package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Reservoir;

import com.mycompany.myapp.repository.ReservoirRepository;
import com.mycompany.myapp.repository.search.ReservoirSearchRepository;
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
 * REST controller for managing Reservoir.
 */
@RestController
@RequestMapping("/api")
public class ReservoirResource {

    private final Logger log = LoggerFactory.getLogger(ReservoirResource.class);

    private static final String ENTITY_NAME = "reservoir";

    private final ReservoirRepository reservoirRepository;

    private final ReservoirSearchRepository reservoirSearchRepository;

    public ReservoirResource(ReservoirRepository reservoirRepository, ReservoirSearchRepository reservoirSearchRepository) {
        this.reservoirRepository = reservoirRepository;
        this.reservoirSearchRepository = reservoirSearchRepository;
    }

    /**
     * POST  /reservoirs : Create a new reservoir.
     *
     * @param reservoir the reservoir to create
     * @return the ResponseEntity with status 201 (Created) and with body the new reservoir, or with status 400 (Bad Request) if the reservoir has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/reservoirs")
    @Timed
    public ResponseEntity<Reservoir> createReservoir(@RequestBody Reservoir reservoir) throws URISyntaxException {
        log.debug("REST request to save Reservoir : {}", reservoir);
        if (reservoir.getId() != null) {
            throw new BadRequestAlertException("A new reservoir cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Reservoir result = reservoirRepository.save(reservoir);
        reservoirSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/reservoirs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /reservoirs : Updates an existing reservoir.
     *
     * @param reservoir the reservoir to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated reservoir,
     * or with status 400 (Bad Request) if the reservoir is not valid,
     * or with status 500 (Internal Server Error) if the reservoir couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/reservoirs")
    @Timed
    public ResponseEntity<Reservoir> updateReservoir(@RequestBody Reservoir reservoir) throws URISyntaxException {
        log.debug("REST request to update Reservoir : {}", reservoir);
        if (reservoir.getId() == null) {
            return createReservoir(reservoir);
        }
        Reservoir result = reservoirRepository.save(reservoir);
        reservoirSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, reservoir.getId().toString()))
            .body(result);
    }

    /**
     * GET  /reservoirs : get all the reservoirs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of reservoirs in body
     */
    @GetMapping("/reservoirs")
    @Timed
    public List<Reservoir> getAllReservoirs() {
        log.debug("REST request to get all Reservoirs");
        return reservoirRepository.findAll();
        }

    /**
     * GET  /reservoirs/:id : get the "id" reservoir.
     *
     * @param id the id of the reservoir to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the reservoir, or with status 404 (Not Found)
     */
    @GetMapping("/reservoirs/{id}")
    @Timed
    public ResponseEntity<Reservoir> getReservoir(@PathVariable Long id) {
        log.debug("REST request to get Reservoir : {}", id);
        Reservoir reservoir = reservoirRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(reservoir));
    }

    /**
     * DELETE  /reservoirs/:id : delete the "id" reservoir.
     *
     * @param id the id of the reservoir to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/reservoirs/{id}")
    @Timed
    public ResponseEntity<Void> deleteReservoir(@PathVariable Long id) {
        log.debug("REST request to delete Reservoir : {}", id);
        reservoirRepository.delete(id);
        reservoirSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/reservoirs?query=:query : search for the reservoir corresponding
     * to the query.
     *
     * @param query the query of the reservoir search
     * @return the result of the search
     */
    @GetMapping("/_search/reservoirs")
    @Timed
    public List<Reservoir> searchReservoirs(@RequestParam String query) {
        log.debug("REST request to search Reservoirs for query {}", query);
        return StreamSupport
            .stream(reservoirSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
