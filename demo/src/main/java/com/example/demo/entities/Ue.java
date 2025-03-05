package com.example.demo.entities;

import java.util.List;

public class Ue {
    private boolean obligatoire;
    private int capacite;
    private String nom;
    private List<Etudiant> etudiants; // List of Etudiant objects

    // Getters and Setters
    public boolean isObligatoire() {
        return obligatoire;
    }

    public void setObligatoire(boolean obligatoire) {
        this.obligatoire = obligatoire;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
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

    // Method to add an Etudiant to the list
    public void addEtudiant(Etudiant etudiant) {
        if (this.etudiants.size() < this.capacite) {
            this.etudiants.add(etudiant);
        } else {
            throw new IllegalStateException("Capacité maximale atteinte");
        }
    }
}
