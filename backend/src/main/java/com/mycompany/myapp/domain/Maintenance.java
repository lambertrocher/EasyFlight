package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Maintenance.
 */
@Entity
@Table(name = "maintenance")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "maintenance")
public class Maintenance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_maintenance")
    private LocalDate dateMaintenance;

    @Column(name = "type_maintenance")
    private String typeMaintenance;

    @Column(name = "commentaire")
    private String commentaire;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateMaintenance() {
        return dateMaintenance;
    }

    public Maintenance dateMaintenance(LocalDate dateMaintenance) {
        this.dateMaintenance = dateMaintenance;
        return this;
    }

    public void setDateMaintenance(LocalDate dateMaintenance) {
        this.dateMaintenance = dateMaintenance;
    }

    public String getTypeMaintenance() {
        return typeMaintenance;
    }

    public Maintenance typeMaintenance(String typeMaintenance) {
        this.typeMaintenance = typeMaintenance;
        return this;
    }

    public void setTypeMaintenance(String typeMaintenance) {
        this.typeMaintenance = typeMaintenance;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public Maintenance commentaire(String commentaire) {
        this.commentaire = commentaire;
        return this;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
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
        Maintenance maintenance = (Maintenance) o;
        if (maintenance.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), maintenance.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Maintenance{" +
            "id=" + getId() +
            ", dateMaintenance='" + getDateMaintenance() + "'" +
            ", typeMaintenance='" + getTypeMaintenance() + "'" +
            ", commentaire='" + getCommentaire() + "'" +
            "}";
    }
}
