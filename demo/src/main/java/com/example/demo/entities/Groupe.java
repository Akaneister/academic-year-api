package com.example.demo.entities;

import java.util.List;


public class Groupe {
    private String gpType;
    private String nom;
    private List<Etudiant> etudiants;

    public String getGpType() {
        return gpType;
    }

    public void setGpType(String gpType) {
        this.gpType = gpType;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Etudiant> getEtudiants() {
        return etudiants;
    }

    public void setEtudiants(List<Etudiant> etudiants) {
        this.etudiants = etudiants;
    }
}
