package com.example.demo.entities;

import java.util.ArrayList;
import java.util.List;

public class Ue {
    private Long id;
    private String nom;
    private boolean obligatoire;
    private int capacite;
    private Long formationId; // Ajout de formation_id

    // Liste des étudiants (doit être récupérée séparément)
    private List<Etudiant> etudiants = new ArrayList<>();

    // Constructeur sans paramètre
    public Ue() {}

    // Constructeur avec paramètres
    public Ue(Long id, String nom, boolean obligatoire, int capacite, Long formationId) {
        this.id = id;
        this.nom = nom;
        this.obligatoire = obligatoire;
        this.capacite = capacite;
        this.formationId = formationId;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

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

    public Long getFormationId() {
        return formationId;
    }

    public void setFormationId(Long formationId) {
        this.formationId = formationId;
    }

    public List<Etudiant> getEtudiants() {
        return etudiants;
    }

    public void setEtudiants(List<Etudiant> etudiants) {
        this.etudiants = etudiants;
    }

    // Ajout sécurisé d'un étudiant
    public void addEtudiant(Etudiant etudiant) {
        if (this.etudiants == null) {
            this.etudiants = new ArrayList<>();
        }

        if (this.etudiants.size() < this.capacite) {
            this.etudiants.add(etudiant);
        } else {
            throw new IllegalStateException("Capacité maximale atteinte");
        }
    }
}
