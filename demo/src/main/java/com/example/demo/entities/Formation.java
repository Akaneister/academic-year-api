package com.example.demo.entities;

import java.util.ArrayList;
import java.util.List;
/*Une Formation est defenis pas :
 * Taille de TP : nombre d'etudiant par groupe
 * Taille de TD : nombre d'etudiant par groupe
 * Nom : nom de la formation
 * Nombre d'Ue : nombre d'option dans la formation
 */
public class Formation {
    private int tailleTP;
    private String nom;
    private int nombreOption;
    private int annee_Academic;

    public int getAnnee_Academic() {
        return annee_Academic;
    }

    public void setAnnee_Academic(int annee_Academic) {
        this.annee_Academic = annee_Academic;
    }
    private List<String> groupes;
    private List<Ue> ues;

    public Formation() {
        this.groupes = new ArrayList<>();
        this.ues = new ArrayList<>();
    }

    public int getTailleTP() {
        return tailleTP;
    }

    public void setTailleTP(int tailleTP) {
        this.tailleTP = tailleTP;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getNombreOption() {
        return nombreOption;
    }

    public void setNombreOption(int nombreOption) {
        this.nombreOption = nombreOption;
    }

    public List<String> getGroupes() {
        return groupes;
    }

    public void setGroupes(List<String> groupes) {
        if (groupes.size() >= 2) {
            this.groupes = groupes;
        } else {
            throw new IllegalArgumentException("La formation doit avoir au minimum 2 groupes.");
        }
    }

    public void addGroupe(String groupe) {
        this.groupes.add(groupe);
    }

    public List<Ue> getUes() {
        return ues;
    }

    public void setUes(List<Ue> ues) {
        this.ues = ues;
    }

    public void addUe(Ue ue) {
        if (this.ues.size() < this.nombreOption) {
            this.ues.add(ue);
        } else {
            throw new IllegalArgumentException("The number of UEs cannot exceed the number of options.");
        }
    }
    
}
