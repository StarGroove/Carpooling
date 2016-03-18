package iut.montreuil.web.rest;

import com.codahale.metrics.annotation.Timed;
import iut.montreuil.domain.Itinary;
import iut.montreuil.repository.ItinaryRepository;
import iut.montreuil.repository.search.ItinarySearchRepository;
import iut.montreuil.web.rest.util.HeaderUtil;
import iut.montreuil.web.rest.dto.ItinaryDTO;
import iut.montreuil.web.rest.mapper.ItinaryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing Itinary.
 */
@RestController
@RequestMapping("/api")
public class ItinaryResource {

    private final Logger log = LoggerFactory.getLogger(ItinaryResource.class);
        
    @Inject
    private ItinaryRepository itinaryRepository;
    
    @Inject
    private ItinaryMapper itinaryMapper;
    
    @Inject
    private ItinarySearchRepository itinarySearchRepository;
    
    /**
     * POST  /itinarys -> Create a new itinary.
     */
    @RequestMapping(value = "/itinarys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ItinaryDTO> createItinary(@RequestBody ItinaryDTO itinaryDTO) throws URISyntaxException {
        log.debug("REST request to save Itinary : {}", itinaryDTO);
        if (itinaryDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("itinary", "idexists", "A new itinary cannot already have an ID")).body(null);
        }
        Itinary itinary = itinaryMapper.itinaryDTOToItinary(itinaryDTO);
        itinary = itinaryRepository.save(itinary);
        ItinaryDTO result = itinaryMapper.itinaryToItinaryDTO(itinary);
        itinarySearchRepository.save(itinary);
        return ResponseEntity.created(new URI("/api/itinarys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("itinary", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /itinarys -> Updates an existing itinary.
     */
    @RequestMapping(value = "/itinarys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ItinaryDTO> updateItinary(@RequestBody ItinaryDTO itinaryDTO) throws URISyntaxException {
        log.debug("REST request to update Itinary : {}", itinaryDTO);
        if (itinaryDTO.getId() == null) {
            return createItinary(itinaryDTO);
        }
        Itinary itinary = itinaryMapper.itinaryDTOToItinary(itinaryDTO);
        itinary = itinaryRepository.save(itinary);
        ItinaryDTO result = itinaryMapper.itinaryToItinaryDTO(itinary);
        itinarySearchRepository.save(itinary);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("itinary", itinaryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /itinarys -> get all the itinarys.
     */
    @RequestMapping(value = "/itinarys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public List<ItinaryDTO> getAllItinarys() {
        log.debug("REST request to get all Itinarys");
        return itinaryRepository.findAll().stream()
            .map(itinaryMapper::itinaryToItinaryDTO)
            .collect(Collectors.toCollection(LinkedList::new));
            }

    /**
     * GET  /itinarys/:id -> get the "id" itinary.
     */
    @RequestMapping(value = "/itinarys/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ItinaryDTO> getItinary(@PathVariable Long id) {
        log.debug("REST request to get Itinary : {}", id);
        Itinary itinary = itinaryRepository.findOne(id);
        ItinaryDTO itinaryDTO = itinaryMapper.itinaryToItinaryDTO(itinary);
        return Optional.ofNullable(itinaryDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /itinarys/:id -> delete the "id" itinary.
     */
    @RequestMapping(value = "/itinarys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteItinary(@PathVariable Long id) {
        log.debug("REST request to delete Itinary : {}", id);
        itinaryRepository.delete(id);
        itinarySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("itinary", id.toString())).build();
    }

    /**
     * SEARCH  /_search/itinarys/:query -> search for the itinary corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/itinarys/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ItinaryDTO> searchItinarys(@PathVariable String query) {
        log.debug("REST request to search Itinarys for query {}", query);
        return StreamSupport
            .stream(itinarySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(itinaryMapper::itinaryToItinaryDTO)
            .collect(Collectors.toList());
    }
}
