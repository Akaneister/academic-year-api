package com.example.demo.entities;

import javax.persistence.Column;

public class Etudiant {
    private String numeroEtudiant;
    private boolean validation;
    private String nom;
    private int id;
    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Column(name = "numeroEtu")
    public String getNom() {
        return nom;
    }

    @Column(name = "compte_id")
    public void setNom(String nom) {
        this.nom = nom;
    }

    @Column(name = "validation")
    public boolean getValidation() {
        return validation;
    }
    


    @Column(name = "numeroEtu")
    public String getNumeroEtudiant() {
        return numeroEtudiant;
    }

    @Column(name = "numeroEtu")
    public void setNumeroEtudiant(String numeroEtudiant) {
        this.numeroEtudiant = numeroEtudiant;
    }

    public boolean isValidation() {
        return validation;
    }

    public void setValidation(boolean validation) {
        this.validation = validation;
    }
}
