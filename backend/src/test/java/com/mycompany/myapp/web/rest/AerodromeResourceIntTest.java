package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.BackendApp;

import com.mycompany.myapp.domain.Aerodrome;
import com.mycompany.myapp.repository.AerodromeRepository;
import com.mycompany.myapp.repository.search.AerodromeSearchRepository;
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
 * Test class for the AerodromeResource REST controller.
 *
 * @see AerodromeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BackendApp.class)
public class AerodromeResourceIntTest {

    private static final String DEFAULT_OACI = "AAAAAAAAAA";
    private static final String UPDATED_OACI = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_AERODROME = "AAAAAAAAAA";
    private static final String UPDATED_NOM_AERODROME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CONTROLE = false;
    private static final Boolean UPDATED_CONTROLE = true;

    private static final Float DEFAULT_FREQUENCE_SOL = 1F;
    private static final Float UPDATED_FREQUENCE_SOL = 2F;

    private static final Float DEFAULT_FREQUENCE_TOUR = 1F;
    private static final Float UPDATED_FREQUENCE_TOUR = 2F;

    private static final Float DEFAULT_ATIS = 1F;
    private static final Float UPDATED_ATIS = 2F;

    private static final Integer DEFAULT_ALTITUDE = 1;
    private static final Integer UPDATED_ALTITUDE = 2;

    @Autowired
    private AerodromeRepository aerodromeRepository;

    @Autowired
    private AerodromeSearchRepository aerodromeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAerodromeMockMvc;

    private Aerodrome aerodrome;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AerodromeResource aerodromeResource = new AerodromeResource(aerodromeRepository, aerodromeSearchRepository);
        this.restAerodromeMockMvc = MockMvcBuilders.standaloneSetup(aerodromeResource)
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
    public static Aerodrome createEntity(EntityManager em) {
        Aerodrome aerodrome = new Aerodrome()
            .oaci(DEFAULT_OACI)
            .nomAerodrome(DEFAULT_NOM_AERODROME)
            .controle(DEFAULT_CONTROLE)
            .frequenceSol(DEFAULT_FREQUENCE_SOL)
            .frequenceTour(DEFAULT_FREQUENCE_TOUR)
            .atis(DEFAULT_ATIS)
            .altitude(DEFAULT_ALTITUDE);
        return aerodrome;
    }

    @Before
    public void initTest() {
        aerodromeSearchRepository.deleteAll();
        aerodrome = createEntity(em);
    }

    @Test
    @Transactional
    public void createAerodrome() throws Exception {
        int databaseSizeBeforeCreate = aerodromeRepository.findAll().size();

        // Create the Aerodrome
        restAerodromeMockMvc.perform(post("/api/aerodromes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(aerodrome)))
            .andExpect(status().isCreated());

        // Validate the Aerodrome in the database
        List<Aerodrome> aerodromeList = aerodromeRepository.findAll();
        assertThat(aerodromeList).hasSize(databaseSizeBeforeCreate + 1);
        Aerodrome testAerodrome = aerodromeList.get(aerodromeList.size() - 1);
        assertThat(testAerodrome.getOaci()).isEqualTo(DEFAULT_OACI);
        assertThat(testAerodrome.getNomAerodrome()).isEqualTo(DEFAULT_NOM_AERODROME);
        assertThat(testAerodrome.isControle()).isEqualTo(DEFAULT_CONTROLE);
        assertThat(testAerodrome.getFrequenceSol()).isEqualTo(DEFAULT_FREQUENCE_SOL);
        assertThat(testAerodrome.getFrequenceTour()).isEqualTo(DEFAULT_FREQUENCE_TOUR);
        assertThat(testAerodrome.getAtis()).isEqualTo(DEFAULT_ATIS);
        assertThat(testAerodrome.getAltitude()).isEqualTo(DEFAULT_ALTITUDE);

        // Validate the Aerodrome in Elasticsearch
        Aerodrome aerodromeEs = aerodromeSearchRepository.findOne(testAerodrome.getId());
        assertThat(aerodromeEs).isEqualToIgnoringGivenFields(testAerodrome);
    }

    @Test
    @Transactional
    public void createAerodromeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = aerodromeRepository.findAll().size();

        // Create the Aerodrome with an existing ID
        aerodrome.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAerodromeMockMvc.perform(post("/api/aerodromes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(aerodrome)))
            .andExpect(status().isBadRequest());

        // Validate the Aerodrome in the database
        List<Aerodrome> aerodromeList = aerodromeRepository.findAll();
        assertThat(aerodromeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAerodromes() throws Exception {
        // Initialize the database
        aerodromeRepository.saveAndFlush(aerodrome);

        // Get all the aerodromeList
        restAerodromeMockMvc.perform(get("/api/aerodromes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aerodrome.getId().intValue())))
            .andExpect(jsonPath("$.[*].oaci").value(hasItem(DEFAULT_OACI.toString())))
            .andExpect(jsonPath("$.[*].nomAerodrome").value(hasItem(DEFAULT_NOM_AERODROME.toString())))
            .andExpect(jsonPath("$.[*].controle").value(hasItem(DEFAULT_CONTROLE.booleanValue())))
            .andExpect(jsonPath("$.[*].frequenceSol").value(hasItem(DEFAULT_FREQUENCE_SOL.doubleValue())))
            .andExpect(jsonPath("$.[*].frequenceTour").value(hasItem(DEFAULT_FREQUENCE_TOUR.doubleValue())))
            .andExpect(jsonPath("$.[*].atis").value(hasItem(DEFAULT_ATIS.doubleValue())))
            .andExpect(jsonPath("$.[*].altitude").value(hasItem(DEFAULT_ALTITUDE)));
    }

    @Test
    @Transactional
    public void getAerodrome() throws Exception {
        // Initialize the database
        aerodromeRepository.saveAndFlush(aerodrome);

        // Get the aerodrome
        restAerodromeMockMvc.perform(get("/api/aerodromes/{id}", aerodrome.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(aerodrome.getId().intValue()))
            .andExpect(jsonPath("$.oaci").value(DEFAULT_OACI.toString()))
            .andExpect(jsonPath("$.nomAerodrome").value(DEFAULT_NOM_AERODROME.toString()))
            .andExpect(jsonPath("$.controle").value(DEFAULT_CONTROLE.booleanValue()))
            .andExpect(jsonPath("$.frequenceSol").value(DEFAULT_FREQUENCE_SOL.doubleValue()))
            .andExpect(jsonPath("$.frequenceTour").value(DEFAULT_FREQUENCE_TOUR.doubleValue()))
            .andExpect(jsonPath("$.atis").value(DEFAULT_ATIS.doubleValue()))
            .andExpect(jsonPath("$.altitude").value(DEFAULT_ALTITUDE));
    }

    @Test
    @Transactional
    public void getNonExistingAerodrome() throws Exception {
        // Get the aerodrome
        restAerodromeMockMvc.perform(get("/api/aerodromes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAerodrome() throws Exception {
        // Initialize the database
        aerodromeRepository.saveAndFlush(aerodrome);
        aerodromeSearchRepository.save(aerodrome);
        int databaseSizeBeforeUpdate = aerodromeRepository.findAll().size();

        // Update the aerodrome
        Aerodrome updatedAerodrome = aerodromeRepository.findOne(aerodrome.getId());
        // Disconnect from session so that the updates on updatedAerodrome are not directly saved in db
        em.detach(updatedAerodrome);
        updatedAerodrome
            .oaci(UPDATED_OACI)
            .nomAerodrome(UPDATED_NOM_AERODROME)
            .controle(UPDATED_CONTROLE)
            .frequenceSol(UPDATED_FREQUENCE_SOL)
            .frequenceTour(UPDATED_FREQUENCE_TOUR)
            .atis(UPDATED_ATIS)
            .altitude(UPDATED_ALTITUDE);

        restAerodromeMockMvc.perform(put("/api/aerodromes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAerodrome)))
            .andExpect(status().isOk());

        // Validate the Aerodrome in the database
        List<Aerodrome> aerodromeList = aerodromeRepository.findAll();
        assertThat(aerodromeList).hasSize(databaseSizeBeforeUpdate);
        Aerodrome testAerodrome = aerodromeList.get(aerodromeList.size() - 1);
        assertThat(testAerodrome.getOaci()).isEqualTo(UPDATED_OACI);
        assertThat(testAerodrome.getNomAerodrome()).isEqualTo(UPDATED_NOM_AERODROME);
        assertThat(testAerodrome.isControle()).isEqualTo(UPDATED_CONTROLE);
        assertThat(testAerodrome.getFrequenceSol()).isEqualTo(UPDATED_FREQUENCE_SOL);
        assertThat(testAerodrome.getFrequenceTour()).isEqualTo(UPDATED_FREQUENCE_TOUR);
        assertThat(testAerodrome.getAtis()).isEqualTo(UPDATED_ATIS);
        assertThat(testAerodrome.getAltitude()).isEqualTo(UPDATED_ALTITUDE);

        // Validate the Aerodrome in Elasticsearch
        Aerodrome aerodromeEs = aerodromeSearchRepository.findOne(testAerodrome.getId());
        assertThat(aerodromeEs).isEqualToIgnoringGivenFields(testAerodrome);
    }

    @Test
    @Transactional
    public void updateNonExistingAerodrome() throws Exception {
        int databaseSizeBeforeUpdate = aerodromeRepository.findAll().size();

        // Create the Aerodrome

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAerodromeMockMvc.perform(put("/api/aerodromes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(aerodrome)))
            .andExpect(status().isCreated());

        // Validate the Aerodrome in the database
        List<Aerodrome> aerodromeList = aerodromeRepository.findAll();
        assertThat(aerodromeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAerodrome() throws Exception {
        // Initialize the database
        aerodromeRepository.saveAndFlush(aerodrome);
        aerodromeSearchRepository.save(aerodrome);
        int databaseSizeBeforeDelete = aerodromeRepository.findAll().size();

        // Get the aerodrome
        restAerodromeMockMvc.perform(delete("/api/aerodromes/{id}", aerodrome.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean aerodromeExistsInEs = aerodromeSearchRepository.exists(aerodrome.getId());
        assertThat(aerodromeExistsInEs).isFalse();

        // Validate the database is empty
        List<Aerodrome> aerodromeList = aerodromeRepository.findAll();
        assertThat(aerodromeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAerodrome() throws Exception {
        // Initialize the database
        aerodromeRepository.saveAndFlush(aerodrome);
        aerodromeSearchRepository.save(aerodrome);

        // Search the aerodrome
        restAerodromeMockMvc.perform(get("/api/_search/aerodromes?query=id:" + aerodrome.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aerodrome.getId().intValue())))
            .andExpect(jsonPath("$.[*].oaci").value(hasItem(DEFAULT_OACI.toString())))
            .andExpect(jsonPath("$.[*].nomAerodrome").value(hasItem(DEFAULT_NOM_AERODROME.toString())))
            .andExpect(jsonPath("$.[*].controle").value(hasItem(DEFAULT_CONTROLE.booleanValue())))
            .andExpect(jsonPath("$.[*].frequenceSol").value(hasItem(DEFAULT_FREQUENCE_SOL.doubleValue())))
            .andExpect(jsonPath("$.[*].frequenceTour").value(hasItem(DEFAULT_FREQUENCE_TOUR.doubleValue())))
            .andExpect(jsonPath("$.[*].atis").value(hasItem(DEFAULT_ATIS.doubleValue())))
            .andExpect(jsonPath("$.[*].altitude").value(hasItem(DEFAULT_ALTITUDE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Aerodrome.class);
        Aerodrome aerodrome1 = new Aerodrome();
        aerodrome1.setId(1L);
        Aerodrome aerodrome2 = new Aerodrome();
        aerodrome2.setId(aerodrome1.getId());
        assertThat(aerodrome1).isEqualTo(aerodrome2);
        aerodrome2.setId(2L);
        assertThat(aerodrome1).isNotEqualTo(aerodrome2);
        aerodrome1.setId(null);
        assertThat(aerodrome1).isNotEqualTo(aerodrome2);
    }
}
