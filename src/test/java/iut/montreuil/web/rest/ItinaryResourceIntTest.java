package iut.montreuil.web.rest;

import iut.montreuil.Application;
import iut.montreuil.domain.Itinary;
import iut.montreuil.repository.ItinaryRepository;
import iut.montreuil.repository.search.ItinarySearchRepository;
import iut.montreuil.web.rest.dto.ItinaryDTO;
import iut.montreuil.web.rest.mapper.ItinaryMapper;

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
 * Test class for the ItinaryResource REST controller.
 *
 * @see ItinaryResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ItinaryResourceIntTest {

    private static final String DEFAULT_LOCATION_START = "AAAAA";
    private static final String UPDATED_LOCATION_START = "BBBBB";
    private static final String DEFAULT_LOCATION_END = "AAAAA";
    private static final String UPDATED_LOCATION_END = "BBBBB";
    private static final String DEFAULT_DRIVER = "AAAAA";
    private static final String UPDATED_DRIVER = "BBBBB";
    private static final String DEFAULT_CAR = "AAAAA";
    private static final String UPDATED_CAR = "BBBBB";
    private static final String DEFAULT_LIST_STEP = "AAAAA";
    private static final String UPDATED_LIST_STEP = "BBBBB";

    @Inject
    private ItinaryRepository itinaryRepository;

    @Inject
    private ItinaryMapper itinaryMapper;

    @Inject
    private ItinarySearchRepository itinarySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restItinaryMockMvc;

    private Itinary itinary;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ItinaryResource itinaryResource = new ItinaryResource();
        ReflectionTestUtils.setField(itinaryResource, "itinarySearchRepository", itinarySearchRepository);
        ReflectionTestUtils.setField(itinaryResource, "itinaryRepository", itinaryRepository);
        ReflectionTestUtils.setField(itinaryResource, "itinaryMapper", itinaryMapper);
        this.restItinaryMockMvc = MockMvcBuilders.standaloneSetup(itinaryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        itinary = new Itinary();
        itinary.setLocationStart(DEFAULT_LOCATION_START);
        itinary.setLocationEnd(DEFAULT_LOCATION_END);
        itinary.setDriver(DEFAULT_DRIVER);
        itinary.setCar(DEFAULT_CAR);
        itinary.setListStep(DEFAULT_LIST_STEP);
    }

    @Test
    @Transactional
    public void createItinary() throws Exception {
        int databaseSizeBeforeCreate = itinaryRepository.findAll().size();

        // Create the Itinary
        ItinaryDTO itinaryDTO = itinaryMapper.itinaryToItinaryDTO(itinary);

        restItinaryMockMvc.perform(post("/api/itinarys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(itinaryDTO)))
                .andExpect(status().isCreated());

        // Validate the Itinary in the database
        List<Itinary> itinarys = itinaryRepository.findAll();
        assertThat(itinarys).hasSize(databaseSizeBeforeCreate + 1);
        Itinary testItinary = itinarys.get(itinarys.size() - 1);
        assertThat(testItinary.getLocationStart()).isEqualTo(DEFAULT_LOCATION_START);
        assertThat(testItinary.getLocationEnd()).isEqualTo(DEFAULT_LOCATION_END);
        assertThat(testItinary.getDriver()).isEqualTo(DEFAULT_DRIVER);
        assertThat(testItinary.getCar()).isEqualTo(DEFAULT_CAR);
        assertThat(testItinary.getListStep()).isEqualTo(DEFAULT_LIST_STEP);
    }

    @Test
    @Transactional
    public void getAllItinarys() throws Exception {
        // Initialize the database
        itinaryRepository.saveAndFlush(itinary);

        // Get all the itinarys
        restItinaryMockMvc.perform(get("/api/itinarys?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(itinary.getId().intValue())))
                .andExpect(jsonPath("$.[*].locationStart").value(hasItem(DEFAULT_LOCATION_START.toString())))
                .andExpect(jsonPath("$.[*].locationEnd").value(hasItem(DEFAULT_LOCATION_END.toString())))
                .andExpect(jsonPath("$.[*].driver").value(hasItem(DEFAULT_DRIVER.toString())))
                .andExpect(jsonPath("$.[*].car").value(hasItem(DEFAULT_CAR.toString())))
                .andExpect(jsonPath("$.[*].listStep").value(hasItem(DEFAULT_LIST_STEP.toString())));
    }

    @Test
    @Transactional
    public void getItinary() throws Exception {
        // Initialize the database
        itinaryRepository.saveAndFlush(itinary);

        // Get the itinary
        restItinaryMockMvc.perform(get("/api/itinarys/{id}", itinary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(itinary.getId().intValue()))
            .andExpect(jsonPath("$.locationStart").value(DEFAULT_LOCATION_START.toString()))
            .andExpect(jsonPath("$.locationEnd").value(DEFAULT_LOCATION_END.toString()))
            .andExpect(jsonPath("$.driver").value(DEFAULT_DRIVER.toString()))
            .andExpect(jsonPath("$.car").value(DEFAULT_CAR.toString()))
            .andExpect(jsonPath("$.listStep").value(DEFAULT_LIST_STEP.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingItinary() throws Exception {
        // Get the itinary
        restItinaryMockMvc.perform(get("/api/itinarys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateItinary() throws Exception {
        // Initialize the database
        itinaryRepository.saveAndFlush(itinary);

		int databaseSizeBeforeUpdate = itinaryRepository.findAll().size();

        // Update the itinary
        itinary.setLocationStart(UPDATED_LOCATION_START);
        itinary.setLocationEnd(UPDATED_LOCATION_END);
        itinary.setDriver(UPDATED_DRIVER);
        itinary.setCar(UPDATED_CAR);
        itinary.setListStep(UPDATED_LIST_STEP);
        ItinaryDTO itinaryDTO = itinaryMapper.itinaryToItinaryDTO(itinary);

        restItinaryMockMvc.perform(put("/api/itinarys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(itinaryDTO)))
                .andExpect(status().isOk());

        // Validate the Itinary in the database
        List<Itinary> itinarys = itinaryRepository.findAll();
        assertThat(itinarys).hasSize(databaseSizeBeforeUpdate);
        Itinary testItinary = itinarys.get(itinarys.size() - 1);
        assertThat(testItinary.getLocationStart()).isEqualTo(UPDATED_LOCATION_START);
        assertThat(testItinary.getLocationEnd()).isEqualTo(UPDATED_LOCATION_END);
        assertThat(testItinary.getDriver()).isEqualTo(UPDATED_DRIVER);
        assertThat(testItinary.getCar()).isEqualTo(UPDATED_CAR);
        assertThat(testItinary.getListStep()).isEqualTo(UPDATED_LIST_STEP);
    }

    @Test
    @Transactional
    public void deleteItinary() throws Exception {
        // Initialize the database
        itinaryRepository.saveAndFlush(itinary);

		int databaseSizeBeforeDelete = itinaryRepository.findAll().size();

        // Get the itinary
        restItinaryMockMvc.perform(delete("/api/itinarys/{id}", itinary.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Itinary> itinarys = itinaryRepository.findAll();
        assertThat(itinarys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
