package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.BackendApp;

import com.mycompany.myapp.domain.Piste;
import com.mycompany.myapp.repository.PisteRepository;
import com.mycompany.myapp.repository.search.PisteSearchRepository;
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
 * Test class for the PisteResource REST controller.
 *
 * @see PisteResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BackendApp.class)
public class PisteResourceIntTest {

    private static final Integer DEFAULT_LONGUEUR = 1;
    private static final Integer UPDATED_LONGUEUR = 2;

    private static final String DEFAULT_TYPE_TERRAIN = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_TERRAIN = "BBBBBBBBBB";

    @Autowired
    private PisteRepository pisteRepository;

    @Autowired
    private PisteSearchRepository pisteSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPisteMockMvc;

    private Piste piste;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PisteResource pisteResource = new PisteResource(pisteRepository, pisteSearchRepository);
        this.restPisteMockMvc = MockMvcBuilders.standaloneSetup(pisteResource)
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
    public static Piste createEntity(EntityManager em) {
        Piste piste = new Piste()
            .longueur(DEFAULT_LONGUEUR)
            .typeTerrain(DEFAULT_TYPE_TERRAIN);
        return piste;
    }

    @Before
    public void initTest() {
        pisteSearchRepository.deleteAll();
        piste = createEntity(em);
    }

    @Test
    @Transactional
    public void createPiste() throws Exception {
        int databaseSizeBeforeCreate = pisteRepository.findAll().size();

        // Create the Piste
        restPisteMockMvc.perform(post("/api/pistes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(piste)))
            .andExpect(status().isCreated());

        // Validate the Piste in the database
        List<Piste> pisteList = pisteRepository.findAll();
        assertThat(pisteList).hasSize(databaseSizeBeforeCreate + 1);
        Piste testPiste = pisteList.get(pisteList.size() - 1);
        assertThat(testPiste.getLongueur()).isEqualTo(DEFAULT_LONGUEUR);
        assertThat(testPiste.getTypeTerrain()).isEqualTo(DEFAULT_TYPE_TERRAIN);

        // Validate the Piste in Elasticsearch
        Piste pisteEs = pisteSearchRepository.findOne(testPiste.getId());
        assertThat(pisteEs).isEqualToIgnoringGivenFields(testPiste);
    }

    @Test
    @Transactional
    public void createPisteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pisteRepository.findAll().size();

        // Create the Piste with an existing ID
        piste.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPisteMockMvc.perform(post("/api/pistes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(piste)))
            .andExpect(status().isBadRequest());

        // Validate the Piste in the database
        List<Piste> pisteList = pisteRepository.findAll();
        assertThat(pisteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPistes() throws Exception {
        // Initialize the database
        pisteRepository.saveAndFlush(piste);

        // Get all the pisteList
        restPisteMockMvc.perform(get("/api/pistes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(piste.getId().intValue())))
            .andExpect(jsonPath("$.[*].longueur").value(hasItem(DEFAULT_LONGUEUR)))
            .andExpect(jsonPath("$.[*].typeTerrain").value(hasItem(DEFAULT_TYPE_TERRAIN.toString())));
    }

    @Test
    @Transactional
    public void getPiste() throws Exception {
        // Initialize the database
        pisteRepository.saveAndFlush(piste);

        // Get the piste
        restPisteMockMvc.perform(get("/api/pistes/{id}", piste.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(piste.getId().intValue()))
            .andExpect(jsonPath("$.longueur").value(DEFAULT_LONGUEUR))
            .andExpect(jsonPath("$.typeTerrain").value(DEFAULT_TYPE_TERRAIN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPiste() throws Exception {
        // Get the piste
        restPisteMockMvc.perform(get("/api/pistes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePiste() throws Exception {
        // Initialize the database
        pisteRepository.saveAndFlush(piste);
        pisteSearchRepository.save(piste);
        int databaseSizeBeforeUpdate = pisteRepository.findAll().size();

        // Update the piste
        Piste updatedPiste = pisteRepository.findOne(piste.getId());
        // Disconnect from session so that the updates on updatedPiste are not directly saved in db
        em.detach(updatedPiste);
        updatedPiste
            .longueur(UPDATED_LONGUEUR)
            .typeTerrain(UPDATED_TYPE_TERRAIN);

        restPisteMockMvc.perform(put("/api/pistes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPiste)))
            .andExpect(status().isOk());

        // Validate the Piste in the database
        List<Piste> pisteList = pisteRepository.findAll();
        assertThat(pisteList).hasSize(databaseSizeBeforeUpdate);
        Piste testPiste = pisteList.get(pisteList.size() - 1);
        assertThat(testPiste.getLongueur()).isEqualTo(UPDATED_LONGUEUR);
        assertThat(testPiste.getTypeTerrain()).isEqualTo(UPDATED_TYPE_TERRAIN);

        // Validate the Piste in Elasticsearch
        Piste pisteEs = pisteSearchRepository.findOne(testPiste.getId());
        assertThat(pisteEs).isEqualToIgnoringGivenFields(testPiste);
    }

    @Test
    @Transactional
    public void updateNonExistingPiste() throws Exception {
        int databaseSizeBeforeUpdate = pisteRepository.findAll().size();

        // Create the Piste

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPisteMockMvc.perform(put("/api/pistes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(piste)))
            .andExpect(status().isCreated());

        // Validate the Piste in the database
        List<Piste> pisteList = pisteRepository.findAll();
        assertThat(pisteList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePiste() throws Exception {
        // Initialize the database
        pisteRepository.saveAndFlush(piste);
        pisteSearchRepository.save(piste);
        int databaseSizeBeforeDelete = pisteRepository.findAll().size();

        // Get the piste
        restPisteMockMvc.perform(delete("/api/pistes/{id}", piste.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean pisteExistsInEs = pisteSearchRepository.exists(piste.getId());
        assertThat(pisteExistsInEs).isFalse();

        // Validate the database is empty
        List<Piste> pisteList = pisteRepository.findAll();
        assertThat(pisteList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPiste() throws Exception {
        // Initialize the database
        pisteRepository.saveAndFlush(piste);
        pisteSearchRepository.save(piste);

        // Search the piste
        restPisteMockMvc.perform(get("/api/_search/pistes?query=id:" + piste.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(piste.getId().intValue())))
            .andExpect(jsonPath("$.[*].longueur").value(hasItem(DEFAULT_LONGUEUR)))
            .andExpect(jsonPath("$.[*].typeTerrain").value(hasItem(DEFAULT_TYPE_TERRAIN.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Piste.class);
        Piste piste1 = new Piste();
        piste1.setId(1L);
        Piste piste2 = new Piste();
        piste2.setId(piste1.getId());
        assertThat(piste1).isEqualTo(piste2);
        piste2.setId(2L);
        assertThat(piste1).isNotEqualTo(piste2);
        piste1.setId(null);
        assertThat(piste1).isNotEqualTo(piste2);
    }
}
