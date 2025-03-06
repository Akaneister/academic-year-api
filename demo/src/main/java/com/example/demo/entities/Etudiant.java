package com.example.demo.entities;

public class Etudiant {
    private String numeroEtudiant;
    private boolean validation;
    private String nom;
    private String prenom;
    private int id;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }


    public boolean getValidation() {
        return validation;
    }
    

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNumeroEtudiant() {
        return numeroEtudiant;
    }

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
