package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Carburant;

import com.mycompany.myapp.repository.CarburantRepository;
import com.mycompany.myapp.repository.search.CarburantSearchRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Carburant.
 */
@RestController
@RequestMapping("/api")
public class CarburantResource {

    private final Logger log = LoggerFactory.getLogger(CarburantResource.class);

    private static final String ENTITY_NAME = "carburant";

    private final CarburantRepository carburantRepository;

    private final CarburantSearchRepository carburantSearchRepository;

    public CarburantResource(CarburantRepository carburantRepository, CarburantSearchRepository carburantSearchRepository) {
        this.carburantRepository = carburantRepository;
        this.carburantSearchRepository = carburantSearchRepository;
    }

    /**
     * POST  /carburants : Create a new carburant.
     *
     * @param carburant the carburant to create
     * @return the ResponseEntity with status 201 (Created) and with body the new carburant, or with status 400 (Bad Request) if the carburant has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/carburants")
    @Timed
    public ResponseEntity<Carburant> createCarburant(@RequestBody Carburant carburant) throws URISyntaxException {
        log.debug("REST request to save Carburant : {}", carburant);
        if (carburant.getId() != null) {
            throw new BadRequestAlertException("A new carburant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Carburant result = carburantRepository.save(carburant);
        carburantSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/carburants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /carburants : Updates an existing carburant.
     *
     * @param carburant the carburant to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated carburant,
     * or with status 400 (Bad Request) if the carburant is not valid,
     * or with status 500 (Internal Server Error) if the carburant couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/carburants")
    @Timed
    public ResponseEntity<Carburant> updateCarburant(@RequestBody Carburant carburant) throws URISyntaxException {
        log.debug("REST request to update Carburant : {}", carburant);
        if (carburant.getId() == null) {
            return createCarburant(carburant);
        }
        Carburant result = carburantRepository.save(carburant);
        carburantSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, carburant.getId().toString()))
            .body(result);
    }

    /**
     * GET  /carburants : get all the carburants.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of carburants in body
     */
    @GetMapping("/carburants")
    @Timed
    public List<Carburant> getAllCarburants() {
        log.debug("REST request to get all Carburants");
        return carburantRepository.findAll();
        }

    /**
     * GET  /carburants/:id : get the "id" carburant.
     *
     * @param id the id of the carburant to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the carburant, or with status 404 (Not Found)
     */
    @GetMapping("/carburants/{id}")
    @Timed
    public ResponseEntity<Carburant> getCarburant(@PathVariable Long id) {
        log.debug("REST request to get Carburant : {}", id);
        Carburant carburant = carburantRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(carburant));
    }

    /**
     * DELETE  /carburants/:id : delete the "id" carburant.
     *
     * @param id the id of the carburant to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/carburants/{id}")
    @Timed
    public ResponseEntity<Void> deleteCarburant(@PathVariable Long id) {
        log.debug("REST request to delete Carburant : {}", id);
        carburantRepository.delete(id);
        carburantSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/carburants?query=:query : search for the carburant corresponding
     * to the query.
     *
     * @param query the query of the carburant search
     * @return the result of the search
     */
    @GetMapping("/_search/carburants")
    @Timed
    public List<Carburant> searchCarburants(@RequestParam String query) {
        log.debug("REST request to search Carburants for query {}", query);
        return StreamSupport
            .stream(carburantSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
