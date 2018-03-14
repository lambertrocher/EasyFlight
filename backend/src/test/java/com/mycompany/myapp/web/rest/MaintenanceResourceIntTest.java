package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.BackendApp;

import com.mycompany.myapp.domain.Maintenance;
import com.mycompany.myapp.repository.MaintenanceRepository;
import com.mycompany.myapp.repository.search.MaintenanceSearchRepository;
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
 * Test class for the MaintenanceResource REST controller.
 *
 * @see MaintenanceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BackendApp.class)
public class MaintenanceResourceIntTest {

    private static final LocalDate DEFAULT_DATE_MAINTENANCE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_MAINTENANCE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_TYPE_MAINTENANCE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_MAINTENANCE = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENTAIRE = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE = "BBBBBBBBBB";

    @Autowired
    private MaintenanceRepository maintenanceRepository;

    @Autowired
    private MaintenanceSearchRepository maintenanceSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMaintenanceMockMvc;

    private Maintenance maintenance;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MaintenanceResource maintenanceResource = new MaintenanceResource(maintenanceRepository, maintenanceSearchRepository);
        this.restMaintenanceMockMvc = MockMvcBuilders.standaloneSetup(maintenanceResource)
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
    public static Maintenance createEntity(EntityManager em) {
        Maintenance maintenance = new Maintenance()
            .dateMaintenance(DEFAULT_DATE_MAINTENANCE)
            .typeMaintenance(DEFAULT_TYPE_MAINTENANCE)
            .commentaire(DEFAULT_COMMENTAIRE);
        return maintenance;
    }

    @Before
    public void initTest() {
        maintenanceSearchRepository.deleteAll();
        maintenance = createEntity(em);
    }

    @Test
    @Transactional
    public void createMaintenance() throws Exception {
        int databaseSizeBeforeCreate = maintenanceRepository.findAll().size();

        // Create the Maintenance
        restMaintenanceMockMvc.perform(post("/api/maintenances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(maintenance)))
            .andExpect(status().isCreated());

        // Validate the Maintenance in the database
        List<Maintenance> maintenanceList = maintenanceRepository.findAll();
        assertThat(maintenanceList).hasSize(databaseSizeBeforeCreate + 1);
        Maintenance testMaintenance = maintenanceList.get(maintenanceList.size() - 1);
        assertThat(testMaintenance.getDateMaintenance()).isEqualTo(DEFAULT_DATE_MAINTENANCE);
        assertThat(testMaintenance.getTypeMaintenance()).isEqualTo(DEFAULT_TYPE_MAINTENANCE);
        assertThat(testMaintenance.getCommentaire()).isEqualTo(DEFAULT_COMMENTAIRE);

        // Validate the Maintenance in Elasticsearch
        Maintenance maintenanceEs = maintenanceSearchRepository.findOne(testMaintenance.getId());
        assertThat(maintenanceEs).isEqualToIgnoringGivenFields(testMaintenance);
    }

    @Test
    @Transactional
    public void createMaintenanceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = maintenanceRepository.findAll().size();

        // Create the Maintenance with an existing ID
        maintenance.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaintenanceMockMvc.perform(post("/api/maintenances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(maintenance)))
            .andExpect(status().isBadRequest());

        // Validate the Maintenance in the database
        List<Maintenance> maintenanceList = maintenanceRepository.findAll();
        assertThat(maintenanceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMaintenances() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get all the maintenanceList
        restMaintenanceMockMvc.perform(get("/api/maintenances?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(maintenance.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateMaintenance").value(hasItem(DEFAULT_DATE_MAINTENANCE.toString())))
            .andExpect(jsonPath("$.[*].typeMaintenance").value(hasItem(DEFAULT_TYPE_MAINTENANCE.toString())))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE.toString())));
    }

    @Test
    @Transactional
    public void getMaintenance() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);

        // Get the maintenance
        restMaintenanceMockMvc.perform(get("/api/maintenances/{id}", maintenance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(maintenance.getId().intValue()))
            .andExpect(jsonPath("$.dateMaintenance").value(DEFAULT_DATE_MAINTENANCE.toString()))
            .andExpect(jsonPath("$.typeMaintenance").value(DEFAULT_TYPE_MAINTENANCE.toString()))
            .andExpect(jsonPath("$.commentaire").value(DEFAULT_COMMENTAIRE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMaintenance() throws Exception {
        // Get the maintenance
        restMaintenanceMockMvc.perform(get("/api/maintenances/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMaintenance() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);
        maintenanceSearchRepository.save(maintenance);
        int databaseSizeBeforeUpdate = maintenanceRepository.findAll().size();

        // Update the maintenance
        Maintenance updatedMaintenance = maintenanceRepository.findOne(maintenance.getId());
        // Disconnect from session so that the updates on updatedMaintenance are not directly saved in db
        em.detach(updatedMaintenance);
        updatedMaintenance
            .dateMaintenance(UPDATED_DATE_MAINTENANCE)
            .typeMaintenance(UPDATED_TYPE_MAINTENANCE)
            .commentaire(UPDATED_COMMENTAIRE);

        restMaintenanceMockMvc.perform(put("/api/maintenances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMaintenance)))
            .andExpect(status().isOk());

        // Validate the Maintenance in the database
        List<Maintenance> maintenanceList = maintenanceRepository.findAll();
        assertThat(maintenanceList).hasSize(databaseSizeBeforeUpdate);
        Maintenance testMaintenance = maintenanceList.get(maintenanceList.size() - 1);
        assertThat(testMaintenance.getDateMaintenance()).isEqualTo(UPDATED_DATE_MAINTENANCE);
        assertThat(testMaintenance.getTypeMaintenance()).isEqualTo(UPDATED_TYPE_MAINTENANCE);
        assertThat(testMaintenance.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);

        // Validate the Maintenance in Elasticsearch
        Maintenance maintenanceEs = maintenanceSearchRepository.findOne(testMaintenance.getId());
        assertThat(maintenanceEs).isEqualToIgnoringGivenFields(testMaintenance);
    }

    @Test
    @Transactional
    public void updateNonExistingMaintenance() throws Exception {
        int databaseSizeBeforeUpdate = maintenanceRepository.findAll().size();

        // Create the Maintenance

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMaintenanceMockMvc.perform(put("/api/maintenances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(maintenance)))
            .andExpect(status().isCreated());

        // Validate the Maintenance in the database
        List<Maintenance> maintenanceList = maintenanceRepository.findAll();
        assertThat(maintenanceList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMaintenance() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);
        maintenanceSearchRepository.save(maintenance);
        int databaseSizeBeforeDelete = maintenanceRepository.findAll().size();

        // Get the maintenance
        restMaintenanceMockMvc.perform(delete("/api/maintenances/{id}", maintenance.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean maintenanceExistsInEs = maintenanceSearchRepository.exists(maintenance.getId());
        assertThat(maintenanceExistsInEs).isFalse();

        // Validate the database is empty
        List<Maintenance> maintenanceList = maintenanceRepository.findAll();
        assertThat(maintenanceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMaintenance() throws Exception {
        // Initialize the database
        maintenanceRepository.saveAndFlush(maintenance);
        maintenanceSearchRepository.save(maintenance);

        // Search the maintenance
        restMaintenanceMockMvc.perform(get("/api/_search/maintenances?query=id:" + maintenance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(maintenance.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateMaintenance").value(hasItem(DEFAULT_DATE_MAINTENANCE.toString())))
            .andExpect(jsonPath("$.[*].typeMaintenance").value(hasItem(DEFAULT_TYPE_MAINTENANCE.toString())))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Maintenance.class);
        Maintenance maintenance1 = new Maintenance();
        maintenance1.setId(1L);
        Maintenance maintenance2 = new Maintenance();
        maintenance2.setId(maintenance1.getId());
        assertThat(maintenance1).isEqualTo(maintenance2);
        maintenance2.setId(2L);
        assertThat(maintenance1).isNotEqualTo(maintenance2);
        maintenance1.setId(null);
        assertThat(maintenance1).isNotEqualTo(maintenance2);
    }
}
