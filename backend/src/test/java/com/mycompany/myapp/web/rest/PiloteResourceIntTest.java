package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.BackendApp;

import com.mycompany.myapp.domain.Pilote;
import com.mycompany.myapp.repository.PiloteRepository;
import com.mycompany.myapp.repository.search.PiloteSearchRepository;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PiloteResource REST controller.
 *
 * @see PiloteResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BackendApp.class)
public class PiloteResourceIntTest {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMERO_TEL = 1;
    private static final Integer UPDATED_NUMERO_TEL = 2;

    private static final String DEFAULT_NUMERO_LICENCE = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_LICENCE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_VALIDITE_LICENCE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_VALIDITE_LICENCE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_ADRESSE_MAIL = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE_MAIL = "BBBBBBBBBB";

    private static final String DEFAULT_CERTIFICAT_MEDICAL = "AAAAAAAAAA";
    private static final String UPDATED_CERTIFICAT_MEDICAL = "BBBBBBBBBB";

    @Autowired
    private PiloteRepository piloteRepository;

    @Autowired
    private PiloteSearchRepository piloteSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPiloteMockMvc;

    private Pilote pilote;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PiloteResource piloteResource = new PiloteResource(piloteRepository, piloteSearchRepository);
        this.restPiloteMockMvc = MockMvcBuilders.standaloneSetup(piloteResource)
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
    public static Pilote createEntity(EntityManager em) {
        Pilote pilote = new Pilote()
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .numeroTel(DEFAULT_NUMERO_TEL)
            .numeroLicence(DEFAULT_NUMERO_LICENCE)
            .dateValiditeLicence(DEFAULT_DATE_VALIDITE_LICENCE)
            .adresseMail(DEFAULT_ADRESSE_MAIL)
            .certificatMedical(DEFAULT_CERTIFICAT_MEDICAL);
        return pilote;
    }

    @Before
    public void initTest() {
        piloteSearchRepository.deleteAll();
        pilote = createEntity(em);
    }

    @Test
    @Transactional
    public void createPilote() throws Exception {
        int databaseSizeBeforeCreate = piloteRepository.findAll().size();

        // Create the Pilote
        restPiloteMockMvc.perform(post("/api/pilotes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pilote)))
            .andExpect(status().isCreated());

        // Validate the Pilote in the database
        List<Pilote> piloteList = piloteRepository.findAll();
        assertThat(piloteList).hasSize(databaseSizeBeforeCreate + 1);
        Pilote testPilote = piloteList.get(piloteList.size() - 1);
        assertThat(testPilote.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testPilote.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testPilote.getNumeroTel()).isEqualTo(DEFAULT_NUMERO_TEL);
        assertThat(testPilote.getNumeroLicence()).isEqualTo(DEFAULT_NUMERO_LICENCE);
        assertThat(testPilote.getDateValiditeLicence()).isEqualTo(DEFAULT_DATE_VALIDITE_LICENCE);
        assertThat(testPilote.getAdresseMail()).isEqualTo(DEFAULT_ADRESSE_MAIL);
        assertThat(testPilote.getCertificatMedical()).isEqualTo(DEFAULT_CERTIFICAT_MEDICAL);

        // Validate the Pilote in Elasticsearch
        Pilote piloteEs = piloteSearchRepository.findOne(testPilote.getId());
        assertThat(piloteEs).isEqualToIgnoringGivenFields(testPilote);
    }

    @Test
    @Transactional
    public void createPiloteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = piloteRepository.findAll().size();

        // Create the Pilote with an existing ID
        pilote.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPiloteMockMvc.perform(post("/api/pilotes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pilote)))
            .andExpect(status().isBadRequest());

        // Validate the Pilote in the database
        List<Pilote> piloteList = piloteRepository.findAll();
        assertThat(piloteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPilotes() throws Exception {
        // Initialize the database
        piloteRepository.saveAndFlush(pilote);

        // Get all the piloteList
        restPiloteMockMvc.perform(get("/api/pilotes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pilote.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM.toString())))
            .andExpect(jsonPath("$.[*].numeroTel").value(hasItem(DEFAULT_NUMERO_TEL)))
            .andExpect(jsonPath("$.[*].numeroLicence").value(hasItem(DEFAULT_NUMERO_LICENCE.toString())))
            .andExpect(jsonPath("$.[*].dateValiditeLicence").value(hasItem(DEFAULT_DATE_VALIDITE_LICENCE.toString())))
            .andExpect(jsonPath("$.[*].adresseMail").value(hasItem(DEFAULT_ADRESSE_MAIL.toString())))
            .andExpect(jsonPath("$.[*].certificatMedical").value(hasItem(DEFAULT_CERTIFICAT_MEDICAL.toString())));
    }

    @Test
    @Transactional
    public void getPilote() throws Exception {
        // Initialize the database
        piloteRepository.saveAndFlush(pilote);

        // Get the pilote
        restPiloteMockMvc.perform(get("/api/pilotes/{id}", pilote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pilote.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM.toString()))
            .andExpect(jsonPath("$.numeroTel").value(DEFAULT_NUMERO_TEL))
            .andExpect(jsonPath("$.numeroLicence").value(DEFAULT_NUMERO_LICENCE.toString()))
            .andExpect(jsonPath("$.dateValiditeLicence").value(DEFAULT_DATE_VALIDITE_LICENCE.toString()))
            .andExpect(jsonPath("$.adresseMail").value(DEFAULT_ADRESSE_MAIL.toString()))
            .andExpect(jsonPath("$.certificatMedical").value(DEFAULT_CERTIFICAT_MEDICAL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPilote() throws Exception {
        // Get the pilote
        restPiloteMockMvc.perform(get("/api/pilotes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePilote() throws Exception {
        // Initialize the database
        piloteRepository.saveAndFlush(pilote);
        piloteSearchRepository.save(pilote);
        int databaseSizeBeforeUpdate = piloteRepository.findAll().size();

        // Update the pilote
        Pilote updatedPilote = piloteRepository.findOne(pilote.getId());
        // Disconnect from session so that the updates on updatedPilote are not directly saved in db
        em.detach(updatedPilote);
        updatedPilote
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .numeroTel(UPDATED_NUMERO_TEL)
            .numeroLicence(UPDATED_NUMERO_LICENCE)
            .dateValiditeLicence(UPDATED_DATE_VALIDITE_LICENCE)
            .adresseMail(UPDATED_ADRESSE_MAIL)
            .certificatMedical(UPDATED_CERTIFICAT_MEDICAL);

        restPiloteMockMvc.perform(put("/api/pilotes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPilote)))
            .andExpect(status().isOk());

        // Validate the Pilote in the database
        List<Pilote> piloteList = piloteRepository.findAll();
        assertThat(piloteList).hasSize(databaseSizeBeforeUpdate);
        Pilote testPilote = piloteList.get(piloteList.size() - 1);
        assertThat(testPilote.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testPilote.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testPilote.getNumeroTel()).isEqualTo(UPDATED_NUMERO_TEL);
        assertThat(testPilote.getNumeroLicence()).isEqualTo(UPDATED_NUMERO_LICENCE);
        assertThat(testPilote.getDateValiditeLicence()).isEqualTo(UPDATED_DATE_VALIDITE_LICENCE);
        assertThat(testPilote.getAdresseMail()).isEqualTo(UPDATED_ADRESSE_MAIL);
        assertThat(testPilote.getCertificatMedical()).isEqualTo(UPDATED_CERTIFICAT_MEDICAL);

        // Validate the Pilote in Elasticsearch
        Pilote piloteEs = piloteSearchRepository.findOne(testPilote.getId());
        assertThat(piloteEs).isEqualToIgnoringGivenFields(testPilote);
    }

    @Test
    @Transactional
    public void updateNonExistingPilote() throws Exception {
        int databaseSizeBeforeUpdate = piloteRepository.findAll().size();

        // Create the Pilote

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPiloteMockMvc.perform(put("/api/pilotes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pilote)))
            .andExpect(status().isCreated());

        // Validate the Pilote in the database
        List<Pilote> piloteList = piloteRepository.findAll();
        assertThat(piloteList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePilote() throws Exception {
        // Initialize the database
        piloteRepository.saveAndFlush(pilote);
        piloteSearchRepository.save(pilote);
        int databaseSizeBeforeDelete = piloteRepository.findAll().size();

        // Get the pilote
        restPiloteMockMvc.perform(delete("/api/pilotes/{id}", pilote.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean piloteExistsInEs = piloteSearchRepository.exists(pilote.getId());
        assertThat(piloteExistsInEs).isFalse();

        // Validate the database is empty
        List<Pilote> piloteList = piloteRepository.findAll();
        assertThat(piloteList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPilote() throws Exception {
        // Initialize the database
        piloteRepository.saveAndFlush(pilote);
        piloteSearchRepository.save(pilote);

        // Search the pilote
        restPiloteMockMvc.perform(get("/api/_search/pilotes?query=id:" + pilote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pilote.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM.toString())))
            .andExpect(jsonPath("$.[*].numeroTel").value(hasItem(DEFAULT_NUMERO_TEL)))
            .andExpect(jsonPath("$.[*].numeroLicence").value(hasItem(DEFAULT_NUMERO_LICENCE.toString())))
            .andExpect(jsonPath("$.[*].dateValiditeLicence").value(hasItem(DEFAULT_DATE_VALIDITE_LICENCE.toString())))
            .andExpect(jsonPath("$.[*].adresseMail").value(hasItem(DEFAULT_ADRESSE_MAIL.toString())))
            .andExpect(jsonPath("$.[*].certificatMedical").value(hasItem(DEFAULT_CERTIFICAT_MEDICAL.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pilote.class);
        Pilote pilote1 = new Pilote();
        pilote1.setId(1L);
        Pilote pilote2 = new Pilote();
        pilote2.setId(pilote1.getId());
        assertThat(pilote1).isEqualTo(pilote2);
        pilote2.setId(2L);
        assertThat(pilote1).isNotEqualTo(pilote2);
        pilote1.setId(null);
        assertThat(pilote1).isNotEqualTo(pilote2);
    }
}
