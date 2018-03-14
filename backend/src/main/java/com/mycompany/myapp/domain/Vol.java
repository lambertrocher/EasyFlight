package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Vol.
 */
@Entity
@Table(name = "vol")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "vol")
public class Vol implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_vol")
    private LocalDate dateVol;

    @ManyToOne
    private Avion avion;

    @ManyToOne
    private Pilote pilote;

    @ManyToOne
    private Aerodrome aerodrome;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateVol() {
        return dateVol;
    }

    public Vol dateVol(LocalDate dateVol) {
        this.dateVol = dateVol;
        return this;
    }

    public void setDateVol(LocalDate dateVol) {
        this.dateVol = dateVol;
    }

    public Avion getAvion() {
        return avion;
    }

    public Vol avion(Avion avion) {
        this.avion = avion;
        return this;
    }

    public void setAvion(Avion avion) {
        this.avion = avion;
    }

    public Pilote getPilote() {
        return pilote;
    }

    public Vol pilote(Pilote pilote) {
        this.pilote = pilote;
        return this;
    }

    public void setPilote(Pilote pilote) {
        this.pilote = pilote;
    }

    public Aerodrome getAerodrome() {
        return aerodrome;
    }

    public Vol aerodrome(Aerodrome aerodrome) {
        this.aerodrome = aerodrome;
        return this;
    }

    public void setAerodrome(Aerodrome aerodrome) {
        this.aerodrome = aerodrome;
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
        Vol vol = (Vol) o;
        if (vol.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), vol.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Vol{" +
            "id=" + getId() +
            ", dateVol='" + getDateVol() + "'" +
            "}";
    }
}
