package iut.montreuil.web.rest;

import iut.montreuil.Application;
import iut.montreuil.domain.CarDriver;
import iut.montreuil.repository.CarDriverRepository;
import iut.montreuil.service.CarDriverService;
import iut.montreuil.web.rest.dto.CarDriverDTO;
import iut.montreuil.web.rest.mapper.CarDriverMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the CarDriverResource REST controller.
 *
 * @see CarDriverResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CarDriverResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_ADRESSE = "AAAAA";
    private static final String UPDATED_ADRESSE = "BBBBB";

    @Inject
    private CarDriverRepository carDriverRepository;

    @Inject
    private CarDriverMapper carDriverMapper;

    @Inject
    private CarDriverService carDriverService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCarDriverMockMvc;

    private CarDriver carDriver;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CarDriverResource carDriverResource = new CarDriverResource();
        ReflectionTestUtils.setField(carDriverResource, "carDriverService", carDriverService);
        ReflectionTestUtils.setField(carDriverResource, "carDriverMapper", carDriverMapper);
        this.restCarDriverMockMvc = MockMvcBuilders.standaloneSetup(carDriverResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        carDriver = new CarDriver();
        carDriver.setName(DEFAULT_NAME);
        carDriver.setAdresse(DEFAULT_ADRESSE);
    }

    @Test
    @Transactional
    public void createCarDriver() throws Exception {
        int databaseSizeBeforeCreate = carDriverRepository.findAll().size();

        // Create the CarDriver
        CarDriverDTO carDriverDTO = carDriverMapper.carDriverToCarDriverDTO(carDriver);

        restCarDriverMockMvc.perform(post("/api/carDrivers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(carDriverDTO)))
                .andExpect(status().isCreated());

        // Validate the CarDriver in the database
        List<CarDriver> carDrivers = carDriverRepository.findAll();
        assertThat(carDrivers).hasSize(databaseSizeBeforeCreate + 1);
        CarDriver testCarDriver = carDrivers.get(carDrivers.size() - 1);
        assertThat(testCarDriver.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCarDriver.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
    }

    @Test
    @Transactional
    public void getAllCarDrivers() throws Exception {
        // Initialize the database
        carDriverRepository.saveAndFlush(carDriver);

        // Get all the carDrivers
        restCarDriverMockMvc.perform(get("/api/carDrivers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(carDriver.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE.toString())));
    }

    @Test
    @Transactional
    public void getCarDriver() throws Exception {
        // Initialize the database
        carDriverRepository.saveAndFlush(carDriver);

        // Get the carDriver
        restCarDriverMockMvc.perform(get("/api/carDrivers/{id}", carDriver.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(carDriver.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCarDriver() throws Exception {
        // Get the carDriver
        restCarDriverMockMvc.perform(get("/api/carDrivers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCarDriver() throws Exception {
        // Initialize the database
        carDriverRepository.saveAndFlush(carDriver);

		int databaseSizeBeforeUpdate = carDriverRepository.findAll().size();

        // Update the carDriver
        carDriver.setName(UPDATED_NAME);
        carDriver.setAdresse(UPDATED_ADRESSE);
        CarDriverDTO carDriverDTO = carDriverMapper.carDriverToCarDriverDTO(carDriver);

        restCarDriverMockMvc.perform(put("/api/carDrivers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(carDriverDTO)))
                .andExpect(status().isOk());

        // Validate the CarDriver in the database
        List<CarDriver> carDrivers = carDriverRepository.findAll();
        assertThat(carDrivers).hasSize(databaseSizeBeforeUpdate);
        CarDriver testCarDriver = carDrivers.get(carDrivers.size() - 1);
        assertThat(testCarDriver.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCarDriver.getAdresse()).isEqualTo(UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    public void deleteCarDriver() throws Exception {
        // Initialize the database
        carDriverRepository.saveAndFlush(carDriver);

		int databaseSizeBeforeDelete = carDriverRepository.findAll().size();

        // Get the carDriver
        restCarDriverMockMvc.perform(delete("/api/carDrivers/{id}", carDriver.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<CarDriver> carDrivers = carDriverRepository.findAll();
        assertThat(carDrivers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
