package com.mycompany.myapp.domain;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * <Enter note text here>
 */
@ApiModel(description = "<Enter note text here>")
@Entity
@Table(name = "carburant")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "carburant")
public class Carburant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_carburant")
    private String typeCarburant;

    @Column(name = "poids_par_litre")
    private Float poidsParLitre;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeCarburant() {
        return typeCarburant;
    }

    public Carburant typeCarburant(String typeCarburant) {
        this.typeCarburant = typeCarburant;
        return this;
    }

    public void setTypeCarburant(String typeCarburant) {
        this.typeCarburant = typeCarburant;
    }

    public Float getPoidsParLitre() {
        return poidsParLitre;
    }

    public Carburant poidsParLitre(Float poidsParLitre) {
        this.poidsParLitre = poidsParLitre;
        return this;
    }

    public void setPoidsParLitre(Float poidsParLitre) {
        this.poidsParLitre = poidsParLitre;
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
        Carburant carburant = (Carburant) o;
        if (carburant.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), carburant.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Carburant{" +
            "id=" + getId() +
            ", typeCarburant='" + getTypeCarburant() + "'" +
            ", poidsParLitre=" + getPoidsParLitre() +
            "}";
    }
}
