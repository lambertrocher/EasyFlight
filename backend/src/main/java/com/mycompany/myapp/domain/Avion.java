package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Avion.
 */
@Entity
@Table(name = "avion")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "avion")
public class Avion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "immatriculation")
    private String immatriculation;

    @Column(name = "type_avion")
    private String typeAvion;

    @Column(name = "nb_sieges_avant")
    private Integer nbSiegesAvant;

    @Column(name = "nb_sieges_arriere")
    private Integer nbSiegesArriere;

    @Column(name = "masse_vide_avion")
    private Integer masseVideAvion;

    @Column(name = "masse_max_bagages")
    private Integer masseMaxBagages;

    @Column(name = "masse_max_totale")
    private Integer masseMaxTotale;

    @Column(name = "type_moteur")
    private String typeMoteur;

    @Column(name = "puissance_avion")
    private Integer puissanceAvion;

    @Column(name = "vitesse_max")
    private Integer vitesseMax;

    @Column(name = "vitesse_croisiere")
    private Integer vitesseCroisiere;

    @Column(name = "facteur_base")
    private Integer facteurBase;

    @Column(name = "nb_heures_vol")
    private Integer nbHeuresVol;

    @Column(name = "levier_passagers_avant")
    private Float levierPassagersAvant;

    @Column(name = "levier_passagers_arriere")
    private Float levierPassagersArriere;

    @Column(name = "levier_bagages")
    private Float levierBagages;

    @ManyToOne
    private Reservoir reservoir;

    @ManyToOne
    private Maintenance maintenance;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImmatriculation() {
        return immatriculation;
    }

    public Avion immatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
        return this;
    }

    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    public String getTypeAvion() {
        return typeAvion;
    }

    public Avion typeAvion(String typeAvion) {
        this.typeAvion = typeAvion;
        return this;
    }

    public void setTypeAvion(String typeAvion) {
        this.typeAvion = typeAvion;
    }

    public Integer getNbSiegesAvant() {
        return nbSiegesAvant;
    }

    public Avion nbSiegesAvant(Integer nbSiegesAvant) {
        this.nbSiegesAvant = nbSiegesAvant;
        return this;
    }

    public void setNbSiegesAvant(Integer nbSiegesAvant) {
        this.nbSiegesAvant = nbSiegesAvant;
    }

    public Integer getNbSiegesArriere() {
        return nbSiegesArriere;
    }

    public Avion nbSiegesArriere(Integer nbSiegesArriere) {
        this.nbSiegesArriere = nbSiegesArriere;
        return this;
    }

    public void setNbSiegesArriere(Integer nbSiegesArriere) {
        this.nbSiegesArriere = nbSiegesArriere;
    }

    public Integer getMasseVideAvion() {
        return masseVideAvion;
    }

    public Avion masseVideAvion(Integer masseVideAvion) {
        this.masseVideAvion = masseVideAvion;
        return this;
    }

    public void setMasseVideAvion(Integer masseVideAvion) {
        this.masseVideAvion = masseVideAvion;
    }

    public Integer getMasseMaxBagages() {
        return masseMaxBagages;
    }

    public Avion masseMaxBagages(Integer masseMaxBagages) {
        this.masseMaxBagages = masseMaxBagages;
        return this;
    }

    public void setMasseMaxBagages(Integer masseMaxBagages) {
        this.masseMaxBagages = masseMaxBagages;
    }

    public Integer getMasseMaxTotale() {
        return masseMaxTotale;
    }

    public Avion masseMaxTotale(Integer masseMaxTotale) {
        this.masseMaxTotale = masseMaxTotale;
        return this;
    }

    public void setMasseMaxTotale(Integer masseMaxTotale) {
        this.masseMaxTotale = masseMaxTotale;
    }

    public String getTypeMoteur() {
        return typeMoteur;
    }

    public Avion typeMoteur(String typeMoteur) {
        this.typeMoteur = typeMoteur;
        return this;
    }

    public void setTypeMoteur(String typeMoteur) {
        this.typeMoteur = typeMoteur;
    }

    public Integer getPuissanceAvion() {
        return puissanceAvion;
    }

    public Avion puissanceAvion(Integer puissanceAvion) {
        this.puissanceAvion = puissanceAvion;
        return this;
    }

    public void setPuissanceAvion(Integer puissanceAvion) {
        this.puissanceAvion = puissanceAvion;
    }

    public Integer getVitesseMax() {
        return vitesseMax;
    }

    public Avion vitesseMax(Integer vitesseMax) {
        this.vitesseMax = vitesseMax;
        return this;
    }

    public void setVitesseMax(Integer vitesseMax) {
        this.vitesseMax = vitesseMax;
    }

    public Integer getVitesseCroisiere() {
        return vitesseCroisiere;
    }

    public Avion vitesseCroisiere(Integer vitesseCroisiere) {
        this.vitesseCroisiere = vitesseCroisiere;
        return this;
    }

    public void setVitesseCroisiere(Integer vitesseCroisiere) {
        this.vitesseCroisiere = vitesseCroisiere;
    }

    public Integer getFacteurBase() {
        return facteurBase;
    }

    public Avion facteurBase(Integer facteurBase) {
        this.facteurBase = facteurBase;
        return this;
    }

    public void setFacteurBase(Integer facteurBase) {
        this.facteurBase = facteurBase;
    }

    public Integer getNbHeuresVol() {
        return nbHeuresVol;
    }

    public Avion nbHeuresVol(Integer nbHeuresVol) {
        this.nbHeuresVol = nbHeuresVol;
        return this;
    }

    public void setNbHeuresVol(Integer nbHeuresVol) {
        this.nbHeuresVol = nbHeuresVol;
    }

    public Float getLevierPassagersAvant() {
        return levierPassagersAvant;
    }

    public Avion levierPassagersAvant(Float levierPassagersAvant) {
        this.levierPassagersAvant = levierPassagersAvant;
        return this;
    }

    public void setLevierPassagersAvant(Float levierPassagersAvant) {
        this.levierPassagersAvant = levierPassagersAvant;
    }

    public Float getLevierPassagersArriere() {
        return levierPassagersArriere;
    }

    public Avion levierPassagersArriere(Float levierPassagersArriere) {
        this.levierPassagersArriere = levierPassagersArriere;
        return this;
    }

    public void setLevierPassagersArriere(Float levierPassagersArriere) {
        this.levierPassagersArriere = levierPassagersArriere;
    }

    public Float getLevierBagages() {
        return levierBagages;
    }

    public Avion levierBagages(Float levierBagages) {
        this.levierBagages = levierBagages;
        return this;
    }

    public void setLevierBagages(Float levierBagages) {
        this.levierBagages = levierBagages;
    }

    public Reservoir getReservoir() {
        return reservoir;
    }

    public Avion reservoir(Reservoir reservoir) {
        this.reservoir = reservoir;
        return this;
    }

    public void setReservoir(Reservoir reservoir) {
        this.reservoir = reservoir;
    }

    public Maintenance getMaintenance() {
        return maintenance;
    }

    public Avion maintenance(Maintenance maintenance) {
        this.maintenance = maintenance;
        return this;
    }

    public void setMaintenance(Maintenance maintenance) {
        this.maintenance = maintenance;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Avion avion = (Avion) o;
        if (avion.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), avion.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Avion{" +
            "id=" + getId() +
            ", immatriculation='" + getImmatriculation() + "'" +
            ", typeAvion='" + getTypeAvion() + "'" +
            ", nbSiegesAvant=" + getNbSiegesAvant() +
            ", nbSiegesArriere=" + getNbSiegesArriere() +
            ", masseVideAvion=" + getMasseVideAvion() +
            ", masseMaxBagages=" + getMasseMaxBagages() +
            ", masseMaxTotale=" + getMasseMaxTotale() +
            ", typeMoteur='" + getTypeMoteur() + "'" +
            ", puissanceAvion=" + getPuissanceAvion() +
            ", vitesseMax=" + getVitesseMax() +
            ", vitesseCroisiere=" + getVitesseCroisiere() +
            ", facteurBase=" + getFacteurBase() +
            ", nbHeuresVol=" + getNbHeuresVol() +
            ", levierPassagersAvant=" + getLevierPassagersAvant() +
            ", levierPassagersArriere=" + getLevierPassagersArriere() +
            ", levierBagages=" + getLevierBagages() +
            "}";
    }
}
