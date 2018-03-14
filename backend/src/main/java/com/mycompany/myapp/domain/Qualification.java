package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Qualification.
 */
@Entity
@Table(name = "qualification")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "qualification")
public class Qualification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "expiration")
    private LocalDate expiration;

    @Column(name = "type_qualification")
    private String typeQualification;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getExpiration() {
        return expiration;
    }

    public Qualification expiration(LocalDate expiration) {
        this.expiration = expiration;
        return this;
    }

    public void setExpiration(LocalDate expiration) {
        this.expiration = expiration;
    }

    public String getTypeQualification() {
        return typeQualification;
    }

    public Qualification typeQualification(String typeQualification) {
        this.typeQualification = typeQualification;
        return this;
    }

    public void setTypeQualification(String typeQualification) {
        this.typeQualification = typeQualification;
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
        Qualification qualification = (Qualification) o;
        if (qualification.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), qualification.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Qualification{" +
            "id=" + getId() +
            ", expiration='" + getExpiration() + "'" +
            ", typeQualification='" + getTypeQualification() + "'" +
            "}";
    }
}
