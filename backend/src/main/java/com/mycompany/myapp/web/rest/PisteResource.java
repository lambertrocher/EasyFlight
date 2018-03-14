package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Piste;

import com.mycompany.myapp.repository.PisteRepository;
import com.mycompany.myapp.repository.search.PisteSearchRepository;
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
 * REST controller for managing Piste.
 */
@RestController
@RequestMapping("/api")
public class PisteResource {

    private final Logger log = LoggerFactory.getLogger(PisteResource.class);

    private static final String ENTITY_NAME = "piste";

    private final PisteRepository pisteRepository;

    private final PisteSearchRepository pisteSearchRepository;

    public PisteResource(PisteRepository pisteRepository, PisteSearchRepository pisteSearchRepository) {
        this.pisteRepository = pisteRepository;
        this.pisteSearchRepository = pisteSearchRepository;
    }

    /**
     * POST  /pistes : Create a new piste.
     *
     * @param piste the piste to create
     * @return the ResponseEntity with status 201 (Created) and with body the new piste, or with status 400 (Bad Request) if the piste has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pistes")
    @Timed
    public ResponseEntity<Piste> createPiste(@RequestBody Piste piste) throws URISyntaxException {
        log.debug("REST request to save Piste : {}", piste);
        if (piste.getId() != null) {
            throw new BadRequestAlertException("A new piste cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Piste result = pisteRepository.save(piste);
        pisteSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/pistes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pistes : Updates an existing piste.
     *
     * @param piste the piste to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated piste,
     * or with status 400 (Bad Request) if the piste is not valid,
     * or with status 500 (Internal Server Error) if the piste couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pistes")
    @Timed
    public ResponseEntity<Piste> updatePiste(@RequestBody Piste piste) throws URISyntaxException {
        log.debug("REST request to update Piste : {}", piste);
        if (piste.getId() == null) {
            return createPiste(piste);
        }
        Piste result = pisteRepository.save(piste);
        pisteSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, piste.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pistes : get all the pistes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of pistes in body
     */
    @GetMapping("/pistes")
    @Timed
    public List<Piste> getAllPistes() {
        log.debug("REST request to get all Pistes");
        return pisteRepository.findAll();
        }

    /**
     * GET  /pistes/:id : get the "id" piste.
     *
     * @param id the id of the piste to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the piste, or with status 404 (Not Found)
     */
    @GetMapping("/pistes/{id}")
    @Timed
    public ResponseEntity<Piste> getPiste(@PathVariable Long id) {
        log.debug("REST request to get Piste : {}", id);
        Piste piste = pisteRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(piste));
    }

    /**
     * DELETE  /pistes/:id : delete the "id" piste.
     *
     * @param id the id of the piste to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pistes/{id}")
    @Timed
    public ResponseEntity<Void> deletePiste(@PathVariable Long id) {
        log.debug("REST request to delete Piste : {}", id);
        pisteRepository.delete(id);
        pisteSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/pistes?query=:query : search for the piste corresponding
     * to the query.
     *
     * @param query the query of the piste search
     * @return the result of the search
     */
    @GetMapping("/_search/pistes")
    @Timed
    public List<Piste> searchPistes(@RequestParam String query) {
        log.debug("REST request to search Pistes for query {}", query);
        return StreamSupport
            .stream(pisteSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
