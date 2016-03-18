package iut.montreuil.service;

import iut.montreuil.domain.CarDriver;
import iut.montreuil.repository.CarDriverRepository;
import iut.montreuil.repository.search.CarDriverSearchRepository;
import iut.montreuil.web.rest.dto.CarDriverDTO;
import iut.montreuil.web.rest.mapper.CarDriverMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing CarDriver.
 */
@Service
@Transactional
public class CarDriverService {

    private final Logger log = LoggerFactory.getLogger(CarDriverService.class);
    
    @Inject
    private CarDriverRepository carDriverRepository;
    
    @Inject
    private CarDriverMapper carDriverMapper;
    
    @Inject
    private CarDriverSearchRepository carDriverSearchRepository;
    
    /**
     * Save a carDriver.
     * @return the persisted entity
     */
    public CarDriverDTO save(CarDriverDTO carDriverDTO) {
        log.debug("Request to save CarDriver : {}", carDriverDTO);
        CarDriver carDriver = carDriverMapper.carDriverDTOToCarDriver(carDriverDTO);
        carDriver = carDriverRepository.save(carDriver);
        CarDriverDTO result = carDriverMapper.carDriverToCarDriverDTO(carDriver);
        carDriverSearchRepository.save(carDriver);
        return result;
    }

    /**
     *  get all the carDrivers.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<CarDriver> findAll(Pageable pageable) {
        log.debug("Request to get all CarDrivers");
        Page<CarDriver> result = carDriverRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one carDriver by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public CarDriverDTO findOne(Long id) {
        log.debug("Request to get CarDriver : {}", id);
        CarDriver carDriver = carDriverRepository.findOne(id);
        CarDriverDTO carDriverDTO = carDriverMapper.carDriverToCarDriverDTO(carDriver);
        return carDriverDTO;
    }

    /**
     *  delete the  carDriver by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete CarDriver : {}", id);
        carDriverRepository.delete(id);
        carDriverSearchRepository.delete(id);
    }

    /**
     * search for the carDriver corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<CarDriverDTO> search(String query) {
        
        log.debug("REST request to search CarDrivers for query {}", query);
        return StreamSupport
            .stream(carDriverSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(carDriverMapper::carDriverToCarDriverDTO)
            .collect(Collectors.toList());
    }
}
