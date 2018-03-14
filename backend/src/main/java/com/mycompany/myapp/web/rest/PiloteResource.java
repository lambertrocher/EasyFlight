package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Pilote;

import com.mycompany.myapp.repository.PiloteRepository;
import com.mycompany.myapp.repository.search.PiloteSearchRepository;
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
 * REST controller for managing Pilote.
 */
@RestController
@RequestMapping("/api")
public class PiloteResource {

    private final Logger log = LoggerFactory.getLogger(PiloteResource.class);

    private static final String ENTITY_NAME = "pilote";

    private final PiloteRepository piloteRepository;

    private final PiloteSearchRepository piloteSearchRepository;

    public PiloteResource(PiloteRepository piloteRepository, PiloteSearchRepository piloteSearchRepository) {
        this.piloteRepository = piloteRepository;
        this.piloteSearchRepository = piloteSearchRepository;
    }

    /**
     * POST  /pilotes : Create a new pilote.
     *
     * @param pilote the pilote to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pilote, or with status 400 (Bad Request) if the pilote has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pilotes")
    @Timed
    public ResponseEntity<Pilote> createPilote(@RequestBody Pilote pilote) throws URISyntaxException {
        log.debug("REST request to save Pilote : {}", pilote);
        if (pilote.getId() != null) {
            throw new BadRequestAlertException("A new pilote cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Pilote result = piloteRepository.save(pilote);
        piloteSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/pilotes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pilotes : Updates an existing pilote.
     *
     * @param pilote the pilote to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pilote,
     * or with status 400 (Bad Request) if the pilote is not valid,
     * or with status 500 (Internal Server Error) if the pilote couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pilotes")
    @Timed
    public ResponseEntity<Pilote> updatePilote(@RequestBody Pilote pilote) throws URISyntaxException {
        log.debug("REST request to update Pilote : {}", pilote);
        if (pilote.getId() == null) {
            return createPilote(pilote);
        }
        Pilote result = piloteRepository.save(pilote);
        piloteSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pilote.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pilotes : get all the pilotes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of pilotes in body
     */
    @GetMapping("/pilotes")
    @Timed
    public List<Pilote> getAllPilotes() {
        log.debug("REST request to get all Pilotes");
        return piloteRepository.findAll();
        }

    /**
     * GET  /pilotes/:id : get the "id" pilote.
     *
     * @param id the id of the pilote to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pilote, or with status 404 (Not Found)
     */
    @GetMapping("/pilotes/{id}")
    @Timed
    public ResponseEntity<Pilote> getPilote(@PathVariable Long id) {
        log.debug("REST request to get Pilote : {}", id);
        Pilote pilote = piloteRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pilote));
    }

    /**
     * DELETE  /pilotes/:id : delete the "id" pilote.
     *
     * @param id the id of the pilote to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pilotes/{id}")
    @Timed
    public ResponseEntity<Void> deletePilote(@PathVariable Long id) {
        log.debug("REST request to delete Pilote : {}", id);
        piloteRepository.delete(id);
        piloteSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/pilotes?query=:query : search for the pilote corresponding
     * to the query.
     *
     * @param query the query of the pilote search
     * @return the result of the search
     */
    @GetMapping("/_search/pilotes")
    @Timed
    public List<Pilote> searchPilotes(@RequestParam String query) {
        log.debug("REST request to search Pilotes for query {}", query);
        return StreamSupport
            .stream(piloteSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
