package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Aerodrome;

import com.mycompany.myapp.repository.AerodromeRepository;
import com.mycompany.myapp.repository.search.AerodromeSearchRepository;
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
 * REST controller for managing Aerodrome.
 */
@RestController
@RequestMapping("/api")
public class AerodromeResource {

    private final Logger log = LoggerFactory.getLogger(AerodromeResource.class);

    private static final String ENTITY_NAME = "aerodrome";

    private final AerodromeRepository aerodromeRepository;

    private final AerodromeSearchRepository aerodromeSearchRepository;

    public AerodromeResource(AerodromeRepository aerodromeRepository, AerodromeSearchRepository aerodromeSearchRepository) {
        this.aerodromeRepository = aerodromeRepository;
        this.aerodromeSearchRepository = aerodromeSearchRepository;
    }

    /**
     * POST  /aerodromes : Create a new aerodrome.
     *
     * @param aerodrome the aerodrome to create
     * @return the ResponseEntity with status 201 (Created) and with body the new aerodrome, or with status 400 (Bad Request) if the aerodrome has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/aerodromes")
    @Timed
    public ResponseEntity<Aerodrome> createAerodrome(@RequestBody Aerodrome aerodrome) throws URISyntaxException {
        log.debug("REST request to save Aerodrome : {}", aerodrome);
        if (aerodrome.getId() != null) {
            throw new BadRequestAlertException("A new aerodrome cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Aerodrome result = aerodromeRepository.save(aerodrome);
        aerodromeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/aerodromes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /aerodromes : Updates an existing aerodrome.
     *
     * @param aerodrome the aerodrome to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated aerodrome,
     * or with status 400 (Bad Request) if the aerodrome is not valid,
     * or with status 500 (Internal Server Error) if the aerodrome couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/aerodromes")
    @Timed
    public ResponseEntity<Aerodrome> updateAerodrome(@RequestBody Aerodrome aerodrome) throws URISyntaxException {
        log.debug("REST request to update Aerodrome : {}", aerodrome);
        if (aerodrome.getId() == null) {
            return createAerodrome(aerodrome);
        }
        Aerodrome result = aerodromeRepository.save(aerodrome);
        aerodromeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, aerodrome.getId().toString()))
            .body(result);
    }

    /**
     * GET  /aerodromes : get all the aerodromes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of aerodromes in body
     */
    @GetMapping("/aerodromes")
    @Timed
    public List<Aerodrome> getAllAerodromes() {
        log.debug("REST request to get all Aerodromes");
        return aerodromeRepository.findAll();
        }

    /**
     * GET  /aerodromes/:id : get the "id" aerodrome.
     *
     * @param id the id of the aerodrome to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the aerodrome, or with status 404 (Not Found)
     */
    @GetMapping("/aerodromes/{id}")
    @Timed
    public ResponseEntity<Aerodrome> getAerodrome(@PathVariable Long id) {
        log.debug("REST request to get Aerodrome : {}", id);
        Aerodrome aerodrome = aerodromeRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(aerodrome));
    }

    /**
     * DELETE  /aerodromes/:id : delete the "id" aerodrome.
     *
     * @param id the id of the aerodrome to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/aerodromes/{id}")
    @Timed
    public ResponseEntity<Void> deleteAerodrome(@PathVariable Long id) {
        log.debug("REST request to delete Aerodrome : {}", id);
        aerodromeRepository.delete(id);
        aerodromeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/aerodromes?query=:query : search for the aerodrome corresponding
     * to the query.
     *
     * @param query the query of the aerodrome search
     * @return the result of the search
     */
    @GetMapping("/_search/aerodromes")
    @Timed
    public List<Aerodrome> searchAerodromes(@RequestParam String query) {
        log.debug("REST request to search Aerodromes for query {}", query);
        return StreamSupport
            .stream(aerodromeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
