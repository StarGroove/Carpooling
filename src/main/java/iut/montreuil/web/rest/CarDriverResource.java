package iut.montreuil.web.rest;

import com.codahale.metrics.annotation.Timed;
import iut.montreuil.domain.CarDriver;
import iut.montreuil.service.CarDriverService;
import iut.montreuil.web.rest.util.HeaderUtil;
import iut.montreuil.web.rest.util.PaginationUtil;
import iut.montreuil.web.rest.dto.CarDriverDTO;
import iut.montreuil.web.rest.mapper.CarDriverMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing CarDriver.
 */
@RestController
@RequestMapping("/api")
public class CarDriverResource {

    private final Logger log = LoggerFactory.getLogger(CarDriverResource.class);
        
    @Inject
    private CarDriverService carDriverService;
    
    @Inject
    private CarDriverMapper carDriverMapper;
    
    /**
     * POST  /carDrivers -> Create a new carDriver.
     */
    @RequestMapping(value = "/carDrivers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CarDriverDTO> createCarDriver(@RequestBody CarDriverDTO carDriverDTO) throws URISyntaxException {
        log.debug("REST request to save CarDriver : {}", carDriverDTO);
        if (carDriverDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("carDriver", "idexists", "A new carDriver cannot already have an ID")).body(null);
        }
        CarDriverDTO result = carDriverService.save(carDriverDTO);
        return ResponseEntity.created(new URI("/api/carDrivers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("carDriver", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /carDrivers -> Updates an existing carDriver.
     */
    @RequestMapping(value = "/carDrivers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CarDriverDTO> updateCarDriver(@RequestBody CarDriverDTO carDriverDTO) throws URISyntaxException {
        log.debug("REST request to update CarDriver : {}", carDriverDTO);
        if (carDriverDTO.getId() == null) {
            return createCarDriver(carDriverDTO);
        }
        CarDriverDTO result = carDriverService.save(carDriverDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("carDriver", carDriverDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /carDrivers -> get all the carDrivers.
     */
    @RequestMapping(value = "/carDrivers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<CarDriverDTO>> getAllCarDrivers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of CarDrivers");
        Page<CarDriver> page = carDriverService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/carDrivers");
        return new ResponseEntity<>(page.getContent().stream()
            .map(carDriverMapper::carDriverToCarDriverDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /carDrivers/:id -> get the "id" carDriver.
     */
    @RequestMapping(value = "/carDrivers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CarDriverDTO> getCarDriver(@PathVariable Long id) {
        log.debug("REST request to get CarDriver : {}", id);
        CarDriverDTO carDriverDTO = carDriverService.findOne(id);
        return Optional.ofNullable(carDriverDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /carDrivers/:id -> delete the "id" carDriver.
     */
    @RequestMapping(value = "/carDrivers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCarDriver(@PathVariable Long id) {
        log.debug("REST request to delete CarDriver : {}", id);
        carDriverService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("carDriver", id.toString())).build();
    }

    /**
     * SEARCH  /_search/carDrivers/:query -> search for the carDriver corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/carDrivers/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CarDriverDTO> searchCarDrivers(@PathVariable String query) {
        log.debug("Request to search CarDrivers for query {}", query);
        return carDriverService.search(query);
    }
}
