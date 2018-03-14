package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Vol;

import com.mycompany.myapp.repository.VolRepository;
import com.mycompany.myapp.repository.search.VolSearchRepository;
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
 * REST controller for managing Vol.
 */
@RestController
@RequestMapping("/api")
public class VolResource {

    private final Logger log = LoggerFactory.getLogger(VolResource.class);

    private static final String ENTITY_NAME = "vol";

    private final VolRepository volRepository;

    private final VolSearchRepository volSearchRepository;

    public VolResource(VolRepository volRepository, VolSearchRepository volSearchRepository) {
        this.volRepository = volRepository;
        this.volSearchRepository = volSearchRepository;
    }

    /**
     * POST  /vols : Create a new vol.
     *
     * @param vol the vol to create
     * @return the ResponseEntity with status 201 (Created) and with body the new vol, or with status 400 (Bad Request) if the vol has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/vols")
    @Timed
    public ResponseEntity<Vol> createVol(@RequestBody Vol vol) throws URISyntaxException {
        log.debug("REST request to save Vol : {}", vol);
        if (vol.getId() != null) {
            throw new BadRequestAlertException("A new vol cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Vol result = volRepository.save(vol);
        volSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/vols/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /vols : Updates an existing vol.
     *
     * @param vol the vol to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated vol,
     * or with status 400 (Bad Request) if the vol is not valid,
     * or with status 500 (Internal Server Error) if the vol couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/vols")
    @Timed
    public ResponseEntity<Vol> updateVol(@RequestBody Vol vol) throws URISyntaxException {
        log.debug("REST request to update Vol : {}", vol);
        if (vol.getId() == null) {
            return createVol(vol);
        }
        Vol result = volRepository.save(vol);
        volSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, vol.getId().toString()))
            .body(result);
    }

    /**
     * GET  /vols : get all the vols.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of vols in body
     */
    @GetMapping("/vols")
    @Timed
    public List<Vol> getAllVols() {
        log.debug("REST request to get all Vols");
        return volRepository.findAll();
        }

    /**
     * GET  /vols/:id : get the "id" vol.
     *
     * @param id the id of the vol to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the vol, or with status 404 (Not Found)
     */
    @GetMapping("/vols/{id}")
    @Timed
    public ResponseEntity<Vol> getVol(@PathVariable Long id) {
        log.debug("REST request to get Vol : {}", id);
        Vol vol = volRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(vol));
    }

    /**
     * DELETE  /vols/:id : delete the "id" vol.
     *
     * @param id the id of the vol to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/vols/{id}")
    @Timed
    public ResponseEntity<Void> deleteVol(@PathVariable Long id) {
        log.debug("REST request to delete Vol : {}", id);
        volRepository.delete(id);
        volSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/vols?query=:query : search for the vol corresponding
     * to the query.
     *
     * @param query the query of the vol search
     * @return the result of the search
     */
    @GetMapping("/_search/vols")
    @Timed
    public List<Vol> searchVols(@RequestParam String query) {
        log.debug("REST request to search Vols for query {}", query);
        return StreamSupport
            .stream(volSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
