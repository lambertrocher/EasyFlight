package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.BackendApp;

import com.mycompany.myapp.domain.Reservoir;
import com.mycompany.myapp.repository.ReservoirRepository;
import com.mycompany.myapp.repository.search.ReservoirSearchRepository;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ReservoirResource REST controller.
 *
 * @see ReservoirResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BackendApp.class)
public class ReservoirResourceIntTest {

    private static final Integer DEFAULT_QUANTITE_MAX = 1;
    private static final Integer UPDATED_QUANTITE_MAX = 2;

    private static final Integer DEFAULT_QUANTITE_PRESENTE = 1;
    private static final Integer UPDATED_QUANTITE_PRESENTE = 2;

    private static final Integer DEFAULT_CAPACITE_MAX_UTILE = 1;
    private static final Integer UPDATED_CAPACITE_MAX_UTILE = 2;

    private static final Float DEFAULT_LEVIER = 1F;
    private static final Float UPDATED_LEVIER = 2F;

    @Autowired
    private ReservoirRepository reservoirRepository;

    @Autowired
    private ReservoirSearchRepository reservoirSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restReservoirMockMvc;

    private Reservoir reservoir;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ReservoirResource reservoirResource = new ReservoirResource(reservoirRepository, reservoirSearchRepository);
        this.restReservoirMockMvc = MockMvcBuilders.standaloneSetup(reservoirResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reservoir createEntity(EntityManager em) {
        Reservoir reservoir = new Reservoir()
            .quantiteMax(DEFAULT_QUANTITE_MAX)
            .quantitePresente(DEFAULT_QUANTITE_PRESENTE)
            .capaciteMaxUtile(DEFAULT_CAPACITE_MAX_UTILE)
            .levier(DEFAULT_LEVIER);
        return reservoir;
    }

    @Before
    public void initTest() {
        reservoirSearchRepository.deleteAll();
        reservoir = createEntity(em);
    }

    @Test
    @Transactional
    public void createReservoir() throws Exception {
        int databaseSizeBeforeCreate = reservoirRepository.findAll().size();

        // Create the Reservoir
        restReservoirMockMvc.perform(post("/api/reservoirs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reservoir)))
            .andExpect(status().isCreated());

        // Validate the Reservoir in the database
        List<Reservoir> reservoirList = reservoirRepository.findAll();
        assertThat(reservoirList).hasSize(databaseSizeBeforeCreate + 1);
        Reservoir testReservoir = reservoirList.get(reservoirList.size() - 1);
        assertThat(testReservoir.getQuantiteMax()).isEqualTo(DEFAULT_QUANTITE_MAX);
        assertThat(testReservoir.getQuantitePresente()).isEqualTo(DEFAULT_QUANTITE_PRESENTE);
        assertThat(testReservoir.getCapaciteMaxUtile()).isEqualTo(DEFAULT_CAPACITE_MAX_UTILE);
        assertThat(testReservoir.getLevier()).isEqualTo(DEFAULT_LEVIER);

        // Validate the Reservoir in Elasticsearch
        Reservoir reservoirEs = reservoirSearchRepository.findOne(testReservoir.getId());
        assertThat(reservoirEs).isEqualToIgnoringGivenFields(testReservoir);
    }

    @Test
    @Transactional
    public void createReservoirWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reservoirRepository.findAll().size();

        // Create the Reservoir with an existing ID
        reservoir.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReservoirMockMvc.perform(post("/api/reservoirs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reservoir)))
            .andExpect(status().isBadRequest());

        // Validate the Reservoir in the database
        List<Reservoir> reservoirList = reservoirRepository.findAll();
        assertThat(reservoirList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllReservoirs() throws Exception {
        // Initialize the database
        reservoirRepository.saveAndFlush(reservoir);

        // Get all the reservoirList
        restReservoirMockMvc.perform(get("/api/reservoirs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reservoir.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantiteMax").value(hasItem(DEFAULT_QUANTITE_MAX)))
            .andExpect(jsonPath("$.[*].quantitePresente").value(hasItem(DEFAULT_QUANTITE_PRESENTE)))
            .andExpect(jsonPath("$.[*].capaciteMaxUtile").value(hasItem(DEFAULT_CAPACITE_MAX_UTILE)))
            .andExpect(jsonPath("$.[*].levier").value(hasItem(DEFAULT_LEVIER.doubleValue())));
    }

    @Test
    @Transactional
    public void getReservoir() throws Exception {
        // Initialize the database
        reservoirRepository.saveAndFlush(reservoir);

        // Get the reservoir
        restReservoirMockMvc.perform(get("/api/reservoirs/{id}", reservoir.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reservoir.getId().intValue()))
            .andExpect(jsonPath("$.quantiteMax").value(DEFAULT_QUANTITE_MAX))
            .andExpect(jsonPath("$.quantitePresente").value(DEFAULT_QUANTITE_PRESENTE))
            .andExpect(jsonPath("$.capaciteMaxUtile").value(DEFAULT_CAPACITE_MAX_UTILE))
            .andExpect(jsonPath("$.levier").value(DEFAULT_LEVIER.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingReservoir() throws Exception {
        // Get the reservoir
        restReservoirMockMvc.perform(get("/api/reservoirs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReservoir() throws Exception {
        // Initialize the database
        reservoirRepository.saveAndFlush(reservoir);
        reservoirSearchRepository.save(reservoir);
        int databaseSizeBeforeUpdate = reservoirRepository.findAll().size();

        // Update the reservoir
        Reservoir updatedReservoir = reservoirRepository.findOne(reservoir.getId());
        // Disconnect from session so that the updates on updatedReservoir are not directly saved in db
        em.detach(updatedReservoir);
        updatedReservoir
            .quantiteMax(UPDATED_QUANTITE_MAX)
            .quantitePresente(UPDATED_QUANTITE_PRESENTE)
            .capaciteMaxUtile(UPDATED_CAPACITE_MAX_UTILE)
            .levier(UPDATED_LEVIER);

        restReservoirMockMvc.perform(put("/api/reservoirs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedReservoir)))
            .andExpect(status().isOk());

        // Validate the Reservoir in the database
        List<Reservoir> reservoirList = reservoirRepository.findAll();
        assertThat(reservoirList).hasSize(databaseSizeBeforeUpdate);
        Reservoir testReservoir = reservoirList.get(reservoirList.size() - 1);
        assertThat(testReservoir.getQuantiteMax()).isEqualTo(UPDATED_QUANTITE_MAX);
        assertThat(testReservoir.getQuantitePresente()).isEqualTo(UPDATED_QUANTITE_PRESENTE);
        assertThat(testReservoir.getCapaciteMaxUtile()).isEqualTo(UPDATED_CAPACITE_MAX_UTILE);
        assertThat(testReservoir.getLevier()).isEqualTo(UPDATED_LEVIER);

        // Validate the Reservoir in Elasticsearch
        Reservoir reservoirEs = reservoirSearchRepository.findOne(testReservoir.getId());
        assertThat(reservoirEs).isEqualToIgnoringGivenFields(testReservoir);
    }

    @Test
    @Transactional
    public void updateNonExistingReservoir() throws Exception {
        int databaseSizeBeforeUpdate = reservoirRepository.findAll().size();

        // Create the Reservoir

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restReservoirMockMvc.perform(put("/api/reservoirs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reservoir)))
            .andExpect(status().isCreated());

        // Validate the Reservoir in the database
        List<Reservoir> reservoirList = reservoirRepository.findAll();
        assertThat(reservoirList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteReservoir() throws Exception {
        // Initialize the database
        reservoirRepository.saveAndFlush(reservoir);
        reservoirSearchRepository.save(reservoir);
        int databaseSizeBeforeDelete = reservoirRepository.findAll().size();

        // Get the reservoir
        restReservoirMockMvc.perform(delete("/api/reservoirs/{id}", reservoir.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean reservoirExistsInEs = reservoirSearchRepository.exists(reservoir.getId());
        assertThat(reservoirExistsInEs).isFalse();

        // Validate the database is empty
        List<Reservoir> reservoirList = reservoirRepository.findAll();
        assertThat(reservoirList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchReservoir() throws Exception {
        // Initialize the database
        reservoirRepository.saveAndFlush(reservoir);
        reservoirSearchRepository.save(reservoir);

        // Search the reservoir
        restReservoirMockMvc.perform(get("/api/_search/reservoirs?query=id:" + reservoir.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reservoir.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantiteMax").value(hasItem(DEFAULT_QUANTITE_MAX)))
            .andExpect(jsonPath("$.[*].quantitePresente").value(hasItem(DEFAULT_QUANTITE_PRESENTE)))
            .andExpect(jsonPath("$.[*].capaciteMaxUtile").value(hasItem(DEFAULT_CAPACITE_MAX_UTILE)))
            .andExpect(jsonPath("$.[*].levier").value(hasItem(DEFAULT_LEVIER.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reservoir.class);
        Reservoir reservoir1 = new Reservoir();
        reservoir1.setId(1L);
        Reservoir reservoir2 = new Reservoir();
        reservoir2.setId(reservoir1.getId());
        assertThat(reservoir1).isEqualTo(reservoir2);
        reservoir2.setId(2L);
        assertThat(reservoir1).isNotEqualTo(reservoir2);
        reservoir1.setId(null);
        assertThat(reservoir1).isNotEqualTo(reservoir2);
    }
}
