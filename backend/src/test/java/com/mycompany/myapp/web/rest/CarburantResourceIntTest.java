package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.BackendApp;

import com.mycompany.myapp.domain.Carburant;
import com.mycompany.myapp.repository.CarburantRepository;
import com.mycompany.myapp.repository.search.CarburantSearchRepository;
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
 * Test class for the CarburantResource REST controller.
 *
 * @see CarburantResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BackendApp.class)
public class CarburantResourceIntTest {

    private static final String DEFAULT_TYPE_CARBURANT = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_CARBURANT = "BBBBBBBBBB";

    private static final Float DEFAULT_POIDS_PAR_LITRE = 1F;
    private static final Float UPDATED_POIDS_PAR_LITRE = 2F;

    @Autowired
    private CarburantRepository carburantRepository;

    @Autowired
    private CarburantSearchRepository carburantSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCarburantMockMvc;

    private Carburant carburant;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CarburantResource carburantResource = new CarburantResource(carburantRepository, carburantSearchRepository);
        this.restCarburantMockMvc = MockMvcBuilders.standaloneSetup(carburantResource)
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
    public static Carburant createEntity(EntityManager em) {
        Carburant carburant = new Carburant()
            .typeCarburant(DEFAULT_TYPE_CARBURANT)
            .poidsParLitre(DEFAULT_POIDS_PAR_LITRE);
        return carburant;
    }

    @Before
    public void initTest() {
        carburantSearchRepository.deleteAll();
        carburant = createEntity(em);
    }

    @Test
    @Transactional
    public void createCarburant() throws Exception {
        int databaseSizeBeforeCreate = carburantRepository.findAll().size();

        // Create the Carburant
        restCarburantMockMvc.perform(post("/api/carburants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(carburant)))
            .andExpect(status().isCreated());

        // Validate the Carburant in the database
        List<Carburant> carburantList = carburantRepository.findAll();
        assertThat(carburantList).hasSize(databaseSizeBeforeCreate + 1);
        Carburant testCarburant = carburantList.get(carburantList.size() - 1);
        assertThat(testCarburant.getTypeCarburant()).isEqualTo(DEFAULT_TYPE_CARBURANT);
        assertThat(testCarburant.getPoidsParLitre()).isEqualTo(DEFAULT_POIDS_PAR_LITRE);

        // Validate the Carburant in Elasticsearch
        Carburant carburantEs = carburantSearchRepository.findOne(testCarburant.getId());
        assertThat(carburantEs).isEqualToIgnoringGivenFields(testCarburant);
    }

    @Test
    @Transactional
    public void createCarburantWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = carburantRepository.findAll().size();

        // Create the Carburant with an existing ID
        carburant.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarburantMockMvc.perform(post("/api/carburants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(carburant)))
            .andExpect(status().isBadRequest());

        // Validate the Carburant in the database
        List<Carburant> carburantList = carburantRepository.findAll();
        assertThat(carburantList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCarburants() throws Exception {
        // Initialize the database
        carburantRepository.saveAndFlush(carburant);

        // Get all the carburantList
        restCarburantMockMvc.perform(get("/api/carburants?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(carburant.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeCarburant").value(hasItem(DEFAULT_TYPE_CARBURANT.toString())))
            .andExpect(jsonPath("$.[*].poidsParLitre").value(hasItem(DEFAULT_POIDS_PAR_LITRE.doubleValue())));
    }

    @Test
    @Transactional
    public void getCarburant() throws Exception {
        // Initialize the database
        carburantRepository.saveAndFlush(carburant);

        // Get the carburant
        restCarburantMockMvc.perform(get("/api/carburants/{id}", carburant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(carburant.getId().intValue()))
            .andExpect(jsonPath("$.typeCarburant").value(DEFAULT_TYPE_CARBURANT.toString()))
            .andExpect(jsonPath("$.poidsParLitre").value(DEFAULT_POIDS_PAR_LITRE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCarburant() throws Exception {
        // Get the carburant
        restCarburantMockMvc.perform(get("/api/carburants/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCarburant() throws Exception {
        // Initialize the database
        carburantRepository.saveAndFlush(carburant);
        carburantSearchRepository.save(carburant);
        int databaseSizeBeforeUpdate = carburantRepository.findAll().size();

        // Update the carburant
        Carburant updatedCarburant = carburantRepository.findOne(carburant.getId());
        // Disconnect from session so that the updates on updatedCarburant are not directly saved in db
        em.detach(updatedCarburant);
        updatedCarburant
            .typeCarburant(UPDATED_TYPE_CARBURANT)
            .poidsParLitre(UPDATED_POIDS_PAR_LITRE);

        restCarburantMockMvc.perform(put("/api/carburants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCarburant)))
            .andExpect(status().isOk());

        // Validate the Carburant in the database
        List<Carburant> carburantList = carburantRepository.findAll();
        assertThat(carburantList).hasSize(databaseSizeBeforeUpdate);
        Carburant testCarburant = carburantList.get(carburantList.size() - 1);
        assertThat(testCarburant.getTypeCarburant()).isEqualTo(UPDATED_TYPE_CARBURANT);
        assertThat(testCarburant.getPoidsParLitre()).isEqualTo(UPDATED_POIDS_PAR_LITRE);

        // Validate the Carburant in Elasticsearch
        Carburant carburantEs = carburantSearchRepository.findOne(testCarburant.getId());
        assertThat(carburantEs).isEqualToIgnoringGivenFields(testCarburant);
    }

    @Test
    @Transactional
    public void updateNonExistingCarburant() throws Exception {
        int databaseSizeBeforeUpdate = carburantRepository.findAll().size();

        // Create the Carburant

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCarburantMockMvc.perform(put("/api/carburants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(carburant)))
            .andExpect(status().isCreated());

        // Validate the Carburant in the database
        List<Carburant> carburantList = carburantRepository.findAll();
        assertThat(carburantList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCarburant() throws Exception {
        // Initialize the database
        carburantRepository.saveAndFlush(carburant);
        carburantSearchRepository.save(carburant);
        int databaseSizeBeforeDelete = carburantRepository.findAll().size();

        // Get the carburant
        restCarburantMockMvc.perform(delete("/api/carburants/{id}", carburant.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean carburantExistsInEs = carburantSearchRepository.exists(carburant.getId());
        assertThat(carburantExistsInEs).isFalse();

        // Validate the database is empty
        List<Carburant> carburantList = carburantRepository.findAll();
        assertThat(carburantList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCarburant() throws Exception {
        // Initialize the database
        carburantRepository.saveAndFlush(carburant);
        carburantSearchRepository.save(carburant);

        // Search the carburant
        restCarburantMockMvc.perform(get("/api/_search/carburants?query=id:" + carburant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(carburant.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeCarburant").value(hasItem(DEFAULT_TYPE_CARBURANT.toString())))
            .andExpect(jsonPath("$.[*].poidsParLitre").value(hasItem(DEFAULT_POIDS_PAR_LITRE.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Carburant.class);
        Carburant carburant1 = new Carburant();
        carburant1.setId(1L);
        Carburant carburant2 = new Carburant();
        carburant2.setId(carburant1.getId());
        assertThat(carburant1).isEqualTo(carburant2);
        carburant2.setId(2L);
        assertThat(carburant1).isNotEqualTo(carburant2);
        carburant1.setId(null);
        assertThat(carburant1).isNotEqualTo(carburant2);
    }
}
