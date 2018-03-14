package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.BackendApp;

import com.mycompany.myapp.domain.Avion;
import com.mycompany.myapp.repository.AvionRepository;
import com.mycompany.myapp.repository.search.AvionSearchRepository;
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
 * Test class for the AvionResource REST controller.
 *
 * @see AvionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BackendApp.class)
public class AvionResourceIntTest {

    private static final String DEFAULT_IMMATRICULATION = "AAAAAAAAAA";
    private static final String UPDATED_IMMATRICULATION = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE_AVION = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_AVION = "BBBBBBBBBB";

    private static final Integer DEFAULT_NB_SIEGES_AVANT = 1;
    private static final Integer UPDATED_NB_SIEGES_AVANT = 2;

    private static final Integer DEFAULT_NB_SIEGES_ARRIERE = 1;
    private static final Integer UPDATED_NB_SIEGES_ARRIERE = 2;

    private static final Integer DEFAULT_MASSE_VIDE_AVION = 1;
    private static final Integer UPDATED_MASSE_VIDE_AVION = 2;

    private static final Integer DEFAULT_MASSE_MAX_BAGAGES = 1;
    private static final Integer UPDATED_MASSE_MAX_BAGAGES = 2;

    private static final Integer DEFAULT_MASSE_MAX_TOTALE = 1;
    private static final Integer UPDATED_MASSE_MAX_TOTALE = 2;

    private static final String DEFAULT_TYPE_MOTEUR = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_MOTEUR = "BBBBBBBBBB";

    private static final Integer DEFAULT_PUISSANCE_AVION = 1;
    private static final Integer UPDATED_PUISSANCE_AVION = 2;

    private static final Integer DEFAULT_VITESSE_MAX = 1;
    private static final Integer UPDATED_VITESSE_MAX = 2;

    private static final Integer DEFAULT_VITESSE_CROISIERE = 1;
    private static final Integer UPDATED_VITESSE_CROISIERE = 2;

    private static final Integer DEFAULT_FACTEUR_BASE = 1;
    private static final Integer UPDATED_FACTEUR_BASE = 2;

    private static final Integer DEFAULT_NB_HEURES_VOL = 1;
    private static final Integer UPDATED_NB_HEURES_VOL = 2;

    private static final Float DEFAULT_LEVIER_PASSAGERS_AVANT = 1F;
    private static final Float UPDATED_LEVIER_PASSAGERS_AVANT = 2F;

    private static final Float DEFAULT_LEVIER_PASSAGERS_ARRIERE = 1F;
    private static final Float UPDATED_LEVIER_PASSAGERS_ARRIERE = 2F;

    private static final Float DEFAULT_LEVIER_BAGAGES = 1F;
    private static final Float UPDATED_LEVIER_BAGAGES = 2F;

    @Autowired
    private AvionRepository avionRepository;

    @Autowired
    private AvionSearchRepository avionSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAvionMockMvc;

    private Avion avion;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AvionResource avionResource = new AvionResource(avionRepository, avionSearchRepository);
        this.restAvionMockMvc = MockMvcBuilders.standaloneSetup(avionResource)
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
    public static Avion createEntity(EntityManager em) {
        Avion avion = new Avion()
            .immatriculation(DEFAULT_IMMATRICULATION)
            .typeAvion(DEFAULT_TYPE_AVION)
            .nbSiegesAvant(DEFAULT_NB_SIEGES_AVANT)
            .nbSiegesArriere(DEFAULT_NB_SIEGES_ARRIERE)
            .masseVideAvion(DEFAULT_MASSE_VIDE_AVION)
            .masseMaxBagages(DEFAULT_MASSE_MAX_BAGAGES)
            .masseMaxTotale(DEFAULT_MASSE_MAX_TOTALE)
            .typeMoteur(DEFAULT_TYPE_MOTEUR)
            .puissanceAvion(DEFAULT_PUISSANCE_AVION)
            .vitesseMax(DEFAULT_VITESSE_MAX)
            .vitesseCroisiere(DEFAULT_VITESSE_CROISIERE)
            .facteurBase(DEFAULT_FACTEUR_BASE)
            .nbHeuresVol(DEFAULT_NB_HEURES_VOL)
            .levierPassagersAvant(DEFAULT_LEVIER_PASSAGERS_AVANT)
            .levierPassagersArriere(DEFAULT_LEVIER_PASSAGERS_ARRIERE)
            .levierBagages(DEFAULT_LEVIER_BAGAGES);
        return avion;
    }

    @Before
    public void initTest() {
        avionSearchRepository.deleteAll();
        avion = createEntity(em);
    }

    @Test
    @Transactional
    public void createAvion() throws Exception {
        int databaseSizeBeforeCreate = avionRepository.findAll().size();

        // Create the Avion
        restAvionMockMvc.perform(post("/api/avions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(avion)))
            .andExpect(status().isCreated());

        // Validate the Avion in the database
        List<Avion> avionList = avionRepository.findAll();
        assertThat(avionList).hasSize(databaseSizeBeforeCreate + 1);
        Avion testAvion = avionList.get(avionList.size() - 1);
        assertThat(testAvion.getImmatriculation()).isEqualTo(DEFAULT_IMMATRICULATION);
        assertThat(testAvion.getTypeAvion()).isEqualTo(DEFAULT_TYPE_AVION);
        assertThat(testAvion.getNbSiegesAvant()).isEqualTo(DEFAULT_NB_SIEGES_AVANT);
        assertThat(testAvion.getNbSiegesArriere()).isEqualTo(DEFAULT_NB_SIEGES_ARRIERE);
        assertThat(testAvion.getMasseVideAvion()).isEqualTo(DEFAULT_MASSE_VIDE_AVION);
        assertThat(testAvion.getMasseMaxBagages()).isEqualTo(DEFAULT_MASSE_MAX_BAGAGES);
        assertThat(testAvion.getMasseMaxTotale()).isEqualTo(DEFAULT_MASSE_MAX_TOTALE);
        assertThat(testAvion.getTypeMoteur()).isEqualTo(DEFAULT_TYPE_MOTEUR);
        assertThat(testAvion.getPuissanceAvion()).isEqualTo(DEFAULT_PUISSANCE_AVION);
        assertThat(testAvion.getVitesseMax()).isEqualTo(DEFAULT_VITESSE_MAX);
        assertThat(testAvion.getVitesseCroisiere()).isEqualTo(DEFAULT_VITESSE_CROISIERE);
        assertThat(testAvion.getFacteurBase()).isEqualTo(DEFAULT_FACTEUR_BASE);
        assertThat(testAvion.getNbHeuresVol()).isEqualTo(DEFAULT_NB_HEURES_VOL);
        assertThat(testAvion.getLevierPassagersAvant()).isEqualTo(DEFAULT_LEVIER_PASSAGERS_AVANT);
        assertThat(testAvion.getLevierPassagersArriere()).isEqualTo(DEFAULT_LEVIER_PASSAGERS_ARRIERE);
        assertThat(testAvion.getLevierBagages()).isEqualTo(DEFAULT_LEVIER_BAGAGES);

        // Validate the Avion in Elasticsearch
        Avion avionEs = avionSearchRepository.findOne(testAvion.getId());
        assertThat(avionEs).isEqualToIgnoringGivenFields(testAvion);
    }

    @Test
    @Transactional
    public void createAvionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = avionRepository.findAll().size();

        // Create the Avion with an existing ID
        avion.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAvionMockMvc.perform(post("/api/avions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(avion)))
            .andExpect(status().isBadRequest());

        // Validate the Avion in the database
        List<Avion> avionList = avionRepository.findAll();
        assertThat(avionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAvions() throws Exception {
        // Initialize the database
        avionRepository.saveAndFlush(avion);

        // Get all the avionList
        restAvionMockMvc.perform(get("/api/avions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(avion.getId().intValue())))
            .andExpect(jsonPath("$.[*].immatriculation").value(hasItem(DEFAULT_IMMATRICULATION.toString())))
            .andExpect(jsonPath("$.[*].typeAvion").value(hasItem(DEFAULT_TYPE_AVION.toString())))
            .andExpect(jsonPath("$.[*].nbSiegesAvant").value(hasItem(DEFAULT_NB_SIEGES_AVANT)))
            .andExpect(jsonPath("$.[*].nbSiegesArriere").value(hasItem(DEFAULT_NB_SIEGES_ARRIERE)))
            .andExpect(jsonPath("$.[*].masseVideAvion").value(hasItem(DEFAULT_MASSE_VIDE_AVION)))
            .andExpect(jsonPath("$.[*].masseMaxBagages").value(hasItem(DEFAULT_MASSE_MAX_BAGAGES)))
            .andExpect(jsonPath("$.[*].masseMaxTotale").value(hasItem(DEFAULT_MASSE_MAX_TOTALE)))
            .andExpect(jsonPath("$.[*].typeMoteur").value(hasItem(DEFAULT_TYPE_MOTEUR.toString())))
            .andExpect(jsonPath("$.[*].puissanceAvion").value(hasItem(DEFAULT_PUISSANCE_AVION)))
            .andExpect(jsonPath("$.[*].vitesseMax").value(hasItem(DEFAULT_VITESSE_MAX)))
            .andExpect(jsonPath("$.[*].vitesseCroisiere").value(hasItem(DEFAULT_VITESSE_CROISIERE)))
            .andExpect(jsonPath("$.[*].facteurBase").value(hasItem(DEFAULT_FACTEUR_BASE)))
            .andExpect(jsonPath("$.[*].nbHeuresVol").value(hasItem(DEFAULT_NB_HEURES_VOL)))
            .andExpect(jsonPath("$.[*].levierPassagersAvant").value(hasItem(DEFAULT_LEVIER_PASSAGERS_AVANT.doubleValue())))
            .andExpect(jsonPath("$.[*].levierPassagersArriere").value(hasItem(DEFAULT_LEVIER_PASSAGERS_ARRIERE.doubleValue())))
            .andExpect(jsonPath("$.[*].levierBagages").value(hasItem(DEFAULT_LEVIER_BAGAGES.doubleValue())));
    }

    @Test
    @Transactional
    public void getAvion() throws Exception {
        // Initialize the database
        avionRepository.saveAndFlush(avion);

        // Get the avion
        restAvionMockMvc.perform(get("/api/avions/{id}", avion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(avion.getId().intValue()))
            .andExpect(jsonPath("$.immatriculation").value(DEFAULT_IMMATRICULATION.toString()))
            .andExpect(jsonPath("$.typeAvion").value(DEFAULT_TYPE_AVION.toString()))
            .andExpect(jsonPath("$.nbSiegesAvant").value(DEFAULT_NB_SIEGES_AVANT))
            .andExpect(jsonPath("$.nbSiegesArriere").value(DEFAULT_NB_SIEGES_ARRIERE))
            .andExpect(jsonPath("$.masseVideAvion").value(DEFAULT_MASSE_VIDE_AVION))
            .andExpect(jsonPath("$.masseMaxBagages").value(DEFAULT_MASSE_MAX_BAGAGES))
            .andExpect(jsonPath("$.masseMaxTotale").value(DEFAULT_MASSE_MAX_TOTALE))
            .andExpect(jsonPath("$.typeMoteur").value(DEFAULT_TYPE_MOTEUR.toString()))
            .andExpect(jsonPath("$.puissanceAvion").value(DEFAULT_PUISSANCE_AVION))
            .andExpect(jsonPath("$.vitesseMax").value(DEFAULT_VITESSE_MAX))
            .andExpect(jsonPath("$.vitesseCroisiere").value(DEFAULT_VITESSE_CROISIERE))
            .andExpect(jsonPath("$.facteurBase").value(DEFAULT_FACTEUR_BASE))
            .andExpect(jsonPath("$.nbHeuresVol").value(DEFAULT_NB_HEURES_VOL))
            .andExpect(jsonPath("$.levierPassagersAvant").value(DEFAULT_LEVIER_PASSAGERS_AVANT.doubleValue()))
            .andExpect(jsonPath("$.levierPassagersArriere").value(DEFAULT_LEVIER_PASSAGERS_ARRIERE.doubleValue()))
            .andExpect(jsonPath("$.levierBagages").value(DEFAULT_LEVIER_BAGAGES.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAvion() throws Exception {
        // Get the avion
        restAvionMockMvc.perform(get("/api/avions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAvion() throws Exception {
        // Initialize the database
        avionRepository.saveAndFlush(avion);
        avionSearchRepository.save(avion);
        int databaseSizeBeforeUpdate = avionRepository.findAll().size();

        // Update the avion
        Avion updatedAvion = avionRepository.findOne(avion.getId());
        // Disconnect from session so that the updates on updatedAvion are not directly saved in db
        em.detach(updatedAvion);
        updatedAvion
            .immatriculation(UPDATED_IMMATRICULATION)
            .typeAvion(UPDATED_TYPE_AVION)
            .nbSiegesAvant(UPDATED_NB_SIEGES_AVANT)
            .nbSiegesArriere(UPDATED_NB_SIEGES_ARRIERE)
            .masseVideAvion(UPDATED_MASSE_VIDE_AVION)
            .masseMaxBagages(UPDATED_MASSE_MAX_BAGAGES)
            .masseMaxTotale(UPDATED_MASSE_MAX_TOTALE)
            .typeMoteur(UPDATED_TYPE_MOTEUR)
            .puissanceAvion(UPDATED_PUISSANCE_AVION)
            .vitesseMax(UPDATED_VITESSE_MAX)
            .vitesseCroisiere(UPDATED_VITESSE_CROISIERE)
            .facteurBase(UPDATED_FACTEUR_BASE)
            .nbHeuresVol(UPDATED_NB_HEURES_VOL)
            .levierPassagersAvant(UPDATED_LEVIER_PASSAGERS_AVANT)
            .levierPassagersArriere(UPDATED_LEVIER_PASSAGERS_ARRIERE)
            .levierBagages(UPDATED_LEVIER_BAGAGES);

        restAvionMockMvc.perform(put("/api/avions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAvion)))
            .andExpect(status().isOk());

        // Validate the Avion in the database
        List<Avion> avionList = avionRepository.findAll();
        assertThat(avionList).hasSize(databaseSizeBeforeUpdate);
        Avion testAvion = avionList.get(avionList.size() - 1);
        assertThat(testAvion.getImmatriculation()).isEqualTo(UPDATED_IMMATRICULATION);
        assertThat(testAvion.getTypeAvion()).isEqualTo(UPDATED_TYPE_AVION);
        assertThat(testAvion.getNbSiegesAvant()).isEqualTo(UPDATED_NB_SIEGES_AVANT);
        assertThat(testAvion.getNbSiegesArriere()).isEqualTo(UPDATED_NB_SIEGES_ARRIERE);
        assertThat(testAvion.getMasseVideAvion()).isEqualTo(UPDATED_MASSE_VIDE_AVION);
        assertThat(testAvion.getMasseMaxBagages()).isEqualTo(UPDATED_MASSE_MAX_BAGAGES);
        assertThat(testAvion.getMasseMaxTotale()).isEqualTo(UPDATED_MASSE_MAX_TOTALE);
        assertThat(testAvion.getTypeMoteur()).isEqualTo(UPDATED_TYPE_MOTEUR);
        assertThat(testAvion.getPuissanceAvion()).isEqualTo(UPDATED_PUISSANCE_AVION);
        assertThat(testAvion.getVitesseMax()).isEqualTo(UPDATED_VITESSE_MAX);
        assertThat(testAvion.getVitesseCroisiere()).isEqualTo(UPDATED_VITESSE_CROISIERE);
        assertThat(testAvion.getFacteurBase()).isEqualTo(UPDATED_FACTEUR_BASE);
        assertThat(testAvion.getNbHeuresVol()).isEqualTo(UPDATED_NB_HEURES_VOL);
        assertThat(testAvion.getLevierPassagersAvant()).isEqualTo(UPDATED_LEVIER_PASSAGERS_AVANT);
        assertThat(testAvion.getLevierPassagersArriere()).isEqualTo(UPDATED_LEVIER_PASSAGERS_ARRIERE);
        assertThat(testAvion.getLevierBagages()).isEqualTo(UPDATED_LEVIER_BAGAGES);

        // Validate the Avion in Elasticsearch
        Avion avionEs = avionSearchRepository.findOne(testAvion.getId());
        assertThat(avionEs).isEqualToIgnoringGivenFields(testAvion);
    }

    @Test
    @Transactional
    public void updateNonExistingAvion() throws Exception {
        int databaseSizeBeforeUpdate = avionRepository.findAll().size();

        // Create the Avion

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAvionMockMvc.perform(put("/api/avions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(avion)))
            .andExpect(status().isCreated());

        // Validate the Avion in the database
        List<Avion> avionList = avionRepository.findAll();
        assertThat(avionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAvion() throws Exception {
        // Initialize the database
        avionRepository.saveAndFlush(avion);
        avionSearchRepository.save(avion);
        int databaseSizeBeforeDelete = avionRepository.findAll().size();

        // Get the avion
        restAvionMockMvc.perform(delete("/api/avions/{id}", avion.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean avionExistsInEs = avionSearchRepository.exists(avion.getId());
        assertThat(avionExistsInEs).isFalse();

        // Validate the database is empty
        List<Avion> avionList = avionRepository.findAll();
        assertThat(avionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAvion() throws Exception {
        // Initialize the database
        avionRepository.saveAndFlush(avion);
        avionSearchRepository.save(avion);

        // Search the avion
        restAvionMockMvc.perform(get("/api/_search/avions?query=id:" + avion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(avion.getId().intValue())))
            .andExpect(jsonPath("$.[*].immatriculation").value(hasItem(DEFAULT_IMMATRICULATION.toString())))
            .andExpect(jsonPath("$.[*].typeAvion").value(hasItem(DEFAULT_TYPE_AVION.toString())))
            .andExpect(jsonPath("$.[*].nbSiegesAvant").value(hasItem(DEFAULT_NB_SIEGES_AVANT)))
            .andExpect(jsonPath("$.[*].nbSiegesArriere").value(hasItem(DEFAULT_NB_SIEGES_ARRIERE)))
            .andExpect(jsonPath("$.[*].masseVideAvion").value(hasItem(DEFAULT_MASSE_VIDE_AVION)))
            .andExpect(jsonPath("$.[*].masseMaxBagages").value(hasItem(DEFAULT_MASSE_MAX_BAGAGES)))
            .andExpect(jsonPath("$.[*].masseMaxTotale").value(hasItem(DEFAULT_MASSE_MAX_TOTALE)))
            .andExpect(jsonPath("$.[*].typeMoteur").value(hasItem(DEFAULT_TYPE_MOTEUR.toString())))
            .andExpect(jsonPath("$.[*].puissanceAvion").value(hasItem(DEFAULT_PUISSANCE_AVION)))
            .andExpect(jsonPath("$.[*].vitesseMax").value(hasItem(DEFAULT_VITESSE_MAX)))
            .andExpect(jsonPath("$.[*].vitesseCroisiere").value(hasItem(DEFAULT_VITESSE_CROISIERE)))
            .andExpect(jsonPath("$.[*].facteurBase").value(hasItem(DEFAULT_FACTEUR_BASE)))
            .andExpect(jsonPath("$.[*].nbHeuresVol").value(hasItem(DEFAULT_NB_HEURES_VOL)))
            .andExpect(jsonPath("$.[*].levierPassagersAvant").value(hasItem(DEFAULT_LEVIER_PASSAGERS_AVANT.doubleValue())))
            .andExpect(jsonPath("$.[*].levierPassagersArriere").value(hasItem(DEFAULT_LEVIER_PASSAGERS_ARRIERE.doubleValue())))
            .andExpect(jsonPath("$.[*].levierBagages").value(hasItem(DEFAULT_LEVIER_BAGAGES.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Avion.class);
        Avion avion1 = new Avion();
        avion1.setId(1L);
        Avion avion2 = new Avion();
        avion2.setId(avion1.getId());
        assertThat(avion1).isEqualTo(avion2);
        avion2.setId(2L);
        assertThat(avion1).isNotEqualTo(avion2);
        avion1.setId(null);
        assertThat(avion1).isNotEqualTo(avion2);
    }
}
