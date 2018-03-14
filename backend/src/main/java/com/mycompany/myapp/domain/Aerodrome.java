package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Aerodrome.
 */
@Entity
@Table(name = "aerodrome")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "aerodrome")
public class Aerodrome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "oaci")
    private String oaci;

    @Column(name = "nom_aerodrome")
    private String nomAerodrome;

    @Column(name = "controle")
    private Boolean controle;

    @Column(name = "frequence_sol")
    private Float frequenceSol;

    @Column(name = "frequence_tour")
    private Float frequenceTour;

    @Column(name = "atis")
    private Float atis;

    @Column(name = "altitude")
    private Integer altitude;

    @ManyToOne
    private Piste piste;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOaci() {
        return oaci;
    }

    public Aerodrome oaci(String oaci) {
        this.oaci = oaci;
        return this;
    }

    public void setOaci(String oaci) {
        this.oaci = oaci;
    }

    public String getNomAerodrome() {
        return nomAerodrome;
    }

    public Aerodrome nomAerodrome(String nomAerodrome) {
        this.nomAerodrome = nomAerodrome;
        return this;
    }

    public void setNomAerodrome(String nomAerodrome) {
        this.nomAerodrome = nomAerodrome;
    }

    public Boolean isControle() {
        return controle;
    }

    public Aerodrome controle(Boolean controle) {
        this.controle = controle;
        return this;
    }

    public void setControle(Boolean controle) {
        this.controle = controle;
    }

    public Float getFrequenceSol() {
        return frequenceSol;
    }

    public Aerodrome frequenceSol(Float frequenceSol) {
        this.frequenceSol = frequenceSol;
        return this;
    }

    public void setFrequenceSol(Float frequenceSol) {
        this.frequenceSol = frequenceSol;
    }

    public Float getFrequenceTour() {
        return frequenceTour;
    }

    public Aerodrome frequenceTour(Float frequenceTour) {
        this.frequenceTour = frequenceTour;
        return this;
    }

    public void setFrequenceTour(Float frequenceTour) {
        this.frequenceTour = frequenceTour;
    }

    public Float getAtis() {
        return atis;
    }

    public Aerodrome atis(Float atis) {
        this.atis = atis;
        return this;
    }

    public void setAtis(Float atis) {
        this.atis = atis;
    }

    public Integer getAltitude() {
        return altitude;
    }

    public Aerodrome altitude(Integer altitude) {
        this.altitude = altitude;
        return this;
    }

    public void setAltitude(Integer altitude) {
        this.altitude = altitude;
    }

    public Piste getPiste() {
        return piste;
    }

    public Aerodrome piste(Piste piste) {
        this.piste = piste;
        return this;
    }

    public void setPiste(Piste piste) {
        this.piste = piste;
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
        Aerodrome aerodrome = (Aerodrome) o;
        if (aerodrome.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), aerodrome.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Aerodrome{" +
            "id=" + getId() +
            ", oaci='" + getOaci() + "'" +
            ", nomAerodrome='" + getNomAerodrome() + "'" +
            ", controle='" + isControle() + "'" +
            ", frequenceSol=" + getFrequenceSol() +
            ", frequenceTour=" + getFrequenceTour() +
            ", atis=" + getAtis() +
            ", altitude=" + getAltitude() +
            "}";
    }
}
