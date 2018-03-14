package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.BackendApp;

import com.mycompany.myapp.domain.Vol;
import com.mycompany.myapp.repository.VolRepository;
import com.mycompany.myapp.repository.search.VolSearchRepository;
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
 * Test class for the VolResource REST controller.
 *
 * @see VolResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BackendApp.class)
public class VolResourceIntTest {

    private static final LocalDate DEFAULT_DATE_VOL = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_VOL = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private VolRepository volRepository;

    @Autowired
    private VolSearchRepository volSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restVolMockMvc;

    private Vol vol;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VolResource volResource = new VolResource(volRepository, volSearchRepository);
        this.restVolMockMvc = MockMvcBuilders.standaloneSetup(volResource)
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
    public static Vol createEntity(EntityManager em) {
        Vol vol = new Vol()
            .dateVol(DEFAULT_DATE_VOL);
        return vol;
    }

    @Before
    public void initTest() {
        volSearchRepository.deleteAll();
        vol = createEntity(em);
    }

    @Test
    @Transactional
    public void createVol() throws Exception {
        int databaseSizeBeforeCreate = volRepository.findAll().size();

        // Create the Vol
        restVolMockMvc.perform(post("/api/vols")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vol)))
            .andExpect(status().isCreated());

        // Validate the Vol in the database
        List<Vol> volList = volRepository.findAll();
        assertThat(volList).hasSize(databaseSizeBeforeCreate + 1);
        Vol testVol = volList.get(volList.size() - 1);
        assertThat(testVol.getDateVol()).isEqualTo(DEFAULT_DATE_VOL);

        // Validate the Vol in Elasticsearch
        Vol volEs = volSearchRepository.findOne(testVol.getId());
        assertThat(volEs).isEqualToIgnoringGivenFields(testVol);
    }

    @Test
    @Transactional
    public void createVolWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = volRepository.findAll().size();

        // Create the Vol with an existing ID
        vol.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVolMockMvc.perform(post("/api/vols")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vol)))
            .andExpect(status().isBadRequest());

        // Validate the Vol in the database
        List<Vol> volList = volRepository.findAll();
        assertThat(volList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllVols() throws Exception {
        // Initialize the database
        volRepository.saveAndFlush(vol);

        // Get all the volList
        restVolMockMvc.perform(get("/api/vols?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vol.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateVol").value(hasItem(DEFAULT_DATE_VOL.toString())));
    }

    @Test
    @Transactional
    public void getVol() throws Exception {
        // Initialize the database
        volRepository.saveAndFlush(vol);

        // Get the vol
        restVolMockMvc.perform(get("/api/vols/{id}", vol.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(vol.getId().intValue()))
            .andExpect(jsonPath("$.dateVol").value(DEFAULT_DATE_VOL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingVol() throws Exception {
        // Get the vol
        restVolMockMvc.perform(get("/api/vols/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVol() throws Exception {
        // Initialize the database
        volRepository.saveAndFlush(vol);
        volSearchRepository.save(vol);
        int databaseSizeBeforeUpdate = volRepository.findAll().size();

        // Update the vol
        Vol updatedVol = volRepository.findOne(vol.getId());
        // Disconnect from session so that the updates on updatedVol are not directly saved in db
        em.detach(updatedVol);
        updatedVol
            .dateVol(UPDATED_DATE_VOL);

        restVolMockMvc.perform(put("/api/vols")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVol)))
            .andExpect(status().isOk());

        // Validate the Vol in the database
        List<Vol> volList = volRepository.findAll();
        assertThat(volList).hasSize(databaseSizeBeforeUpdate);
        Vol testVol = volList.get(volList.size() - 1);
        assertThat(testVol.getDateVol()).isEqualTo(UPDATED_DATE_VOL);

        // Validate the Vol in Elasticsearch
        Vol volEs = volSearchRepository.findOne(testVol.getId());
        assertThat(volEs).isEqualToIgnoringGivenFields(testVol);
    }

    @Test
    @Transactional
    public void updateNonExistingVol() throws Exception {
        int databaseSizeBeforeUpdate = volRepository.findAll().size();

        // Create the Vol

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restVolMockMvc.perform(put("/api/vols")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vol)))
            .andExpect(status().isCreated());

        // Validate the Vol in the database
        List<Vol> volList = volRepository.findAll();
        assertThat(volList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteVol() throws Exception {
        // Initialize the database
        volRepository.saveAndFlush(vol);
        volSearchRepository.save(vol);
        int databaseSizeBeforeDelete = volRepository.findAll().size();

        // Get the vol
        restVolMockMvc.perform(delete("/api/vols/{id}", vol.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean volExistsInEs = volSearchRepository.exists(vol.getId());
        assertThat(volExistsInEs).isFalse();

        // Validate the database is empty
        List<Vol> volList = volRepository.findAll();
        assertThat(volList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchVol() throws Exception {
        // Initialize the database
        volRepository.saveAndFlush(vol);
        volSearchRepository.save(vol);

        // Search the vol
        restVolMockMvc.perform(get("/api/_search/vols?query=id:" + vol.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vol.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateVol").value(hasItem(DEFAULT_DATE_VOL.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vol.class);
        Vol vol1 = new Vol();
        vol1.setId(1L);
        Vol vol2 = new Vol();
        vol2.setId(vol1.getId());
        assertThat(vol1).isEqualTo(vol2);
        vol2.setId(2L);
        assertThat(vol1).isNotEqualTo(vol2);
        vol1.setId(null);
        assertThat(vol1).isNotEqualTo(vol2);
    }
}
