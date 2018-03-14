package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Reservoir.
 */
@Entity
@Table(name = "reservoir")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "reservoir")
public class Reservoir implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantite_max")
    private Integer quantiteMax;

    @Column(name = "quantite_presente")
    private Integer quantitePresente;

    @Column(name = "capacite_max_utile")
    private Integer capaciteMaxUtile;

    @Column(name = "levier")
    private Float levier;

    @ManyToOne
    private Carburant carburant;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantiteMax() {
        return quantiteMax;
    }

    public Reservoir quantiteMax(Integer quantiteMax) {
        this.quantiteMax = quantiteMax;
        return this;
    }

    public void setQuantiteMax(Integer quantiteMax) {
        this.quantiteMax = quantiteMax;
    }

    public Integer getQuantitePresente() {
        return quantitePresente;
    }

    public Reservoir quantitePresente(Integer quantitePresente) {
        this.quantitePresente = quantitePresente;
        return this;
    }

    public void setQuantitePresente(Integer quantitePresente) {
        this.quantitePresente = quantitePresente;
    }

    public Integer getCapaciteMaxUtile() {
        return capaciteMaxUtile;
    }

    public Reservoir capaciteMaxUtile(Integer capaciteMaxUtile) {
        this.capaciteMaxUtile = capaciteMaxUtile;
        return this;
    }

    public void setCapaciteMaxUtile(Integer capaciteMaxUtile) {
        this.capaciteMaxUtile = capaciteMaxUtile;
    }

    public Float getLevier() {
        return levier;
    }

    public Reservoir levier(Float levier) {
        this.levier = levier;
        return this;
    }

    public void setLevier(Float levier) {
        this.levier = levier;
    }

    public Carburant getCarburant() {
        return carburant;
    }

    public Reservoir carburant(Carburant carburant) {
        this.carburant = carburant;
        return this;
    }

    public void setCarburant(Carburant carburant) {
        this.carburant = carburant;
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
        Reservoir reservoir = (Reservoir) o;
        if (reservoir.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reservoir.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Reservoir{" +
            "id=" + getId() +
            ", quantiteMax=" + getQuantiteMax() +
            ", quantitePresente=" + getQuantitePresente() +
            ", capaciteMaxUtile=" + getCapaciteMaxUtile() +
            ", levier=" + getLevier() +
            "}";
    }
}
