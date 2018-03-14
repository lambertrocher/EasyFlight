package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Avion;

import com.mycompany.myapp.repository.AvionRepository;
import com.mycompany.myapp.repository.search.AvionSearchRepository;
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
 * REST controller for managing Avion.
 */
@RestController
@RequestMapping("/api")
public class AvionResource {

    private final Logger log = LoggerFactory.getLogger(AvionResource.class);

    private static final String ENTITY_NAME = "avion";

    private final AvionRepository avionRepository;

    private final AvionSearchRepository avionSearchRepository;

    public AvionResource(AvionRepository avionRepository, AvionSearchRepository avionSearchRepository) {
        this.avionRepository = avionRepository;
        this.avionSearchRepository = avionSearchRepository;
    }

    /**
     * POST  /avions : Create a new avion.
     *
     * @param avion the avion to create
     * @return the ResponseEntity with status 201 (Created) and with body the new avion, or with status 400 (Bad Request) if the avion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/avions")
    @Timed
    public ResponseEntity<Avion> createAvion(@RequestBody Avion avion) throws URISyntaxException {
        log.debug("REST request to save Avion : {}", avion);
        if (avion.getId() != null) {
            throw new BadRequestAlertException("A new avion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Avion result = avionRepository.save(avion);
        avionSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/avions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /avions : Updates an existing avion.
     *
     * @param avion the avion to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated avion,
     * or with status 400 (Bad Request) if the avion is not valid,
     * or with status 500 (Internal Server Error) if the avion couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/avions")
    @Timed
    public ResponseEntity<Avion> updateAvion(@RequestBody Avion avion) throws URISyntaxException {
        log.debug("REST request to update Avion : {}", avion);
        if (avion.getId() == null) {
            return createAvion(avion);
        }
        Avion result = avionRepository.save(avion);
        avionSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, avion.getId().toString()))
            .body(result);
    }

    /**
     * GET  /avions : get all the avions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of avions in body
     */
    @GetMapping("/avions")
    @Timed
    public List<Avion> getAllAvions() {
        log.debug("REST request to get all Avions");
        return avionRepository.findAll();
        }

    /**
     * GET  /avions/:id : get the "id" avion.
     *
     * @param id the id of the avion to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the avion, or with status 404 (Not Found)
     */
    @GetMapping("/avions/{id}")
    @Timed
    public ResponseEntity<Avion> getAvion(@PathVariable Long id) {
        log.debug("REST request to get Avion : {}", id);
        Avion avion = avionRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(avion));
    }

    /**
     * DELETE  /avions/:id : delete the "id" avion.
     *
     * @param id the id of the avion to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/avions/{id}")
    @Timed
    public ResponseEntity<Void> deleteAvion(@PathVariable Long id) {
        log.debug("REST request to delete Avion : {}", id);
        avionRepository.delete(id);
        avionSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/avions?query=:query : search for the avion corresponding
     * to the query.
     *
     * @param query the query of the avion search
     * @return the result of the search
     */
    @GetMapping("/_search/avions")
    @Timed
    public List<Avion> searchAvions(@RequestParam String query) {
        log.debug("REST request to search Avions for query {}", query);
        return StreamSupport
            .stream(avionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
