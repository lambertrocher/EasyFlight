package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Pilote.
 */
@Entity
@Table(name = "pilote")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "pilote")
public class Pilote implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "numero_tel")
    private Integer numeroTel;

    @Column(name = "numero_licence")
    private String numeroLicence;

    @Column(name = "date_validite_licence")
    private LocalDate dateValiditeLicence;

    @Column(name = "adresse_mail")
    private String adresseMail;

    @Column(name = "certificat_medical")
    private String certificatMedical;

    @ManyToOne
    private Qualification qualification;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public Pilote nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public Pilote prenom(String prenom) {
        this.prenom = prenom;
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Integer getNumeroTel() {
        return numeroTel;
    }

    public Pilote numeroTel(Integer numeroTel) {
        this.numeroTel = numeroTel;
        return this;
    }

    public void setNumeroTel(Integer numeroTel) {
        this.numeroTel = numeroTel;
    }

    public String getNumeroLicence() {
        return numeroLicence;
    }

    public Pilote numeroLicence(String numeroLicence) {
        this.numeroLicence = numeroLicence;
        return this;
    }

    public void setNumeroLicence(String numeroLicence) {
        this.numeroLicence = numeroLicence;
    }

    public LocalDate getDateValiditeLicence() {
        return dateValiditeLicence;
    }

    public Pilote dateValiditeLicence(LocalDate dateValiditeLicence) {
        this.dateValiditeLicence = dateValiditeLicence;
        return this;
    }

    public void setDateValiditeLicence(LocalDate dateValiditeLicence) {
        this.dateValiditeLicence = dateValiditeLicence;
    }

    public String getAdresseMail() {
        return adresseMail;
    }

    public Pilote adresseMail(String adresseMail) {
        this.adresseMail = adresseMail;
        return this;
    }

    public void setAdresseMail(String adresseMail) {
        this.adresseMail = adresseMail;
    }

    public String getCertificatMedical() {
        return certificatMedical;
    }

    public Pilote certificatMedical(String certificatMedical) {
        this.certificatMedical = certificatMedical;
        return this;
    }

    public void setCertificatMedical(String certificatMedical) {
        this.certificatMedical = certificatMedical;
    }

    public Qualification getQualification() {
        return qualification;
    }

    public Pilote qualification(Qualification qualification) {
        this.qualification = qualification;
        return this;
    }

    public void setQualification(Qualification qualification) {
        this.qualification = qualification;
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
        Pilote pilote = (Pilote) o;
        if (pilote.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pilote.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Pilote{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", numeroTel=" + getNumeroTel() +
            ", numeroLicence='" + getNumeroLicence() + "'" +
            ", dateValiditeLicence='" + getDateValiditeLicence() + "'" +
            ", adresseMail='" + getAdresseMail() + "'" +
            ", certificatMedical='" + getCertificatMedical() + "'" +
            "}";
    }
}
