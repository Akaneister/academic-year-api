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
    private int tailleTD;
    private String nom;
    private int nbUeOption;
    private String annee_Academic;
    private int id;  
    private List<String> groupes;
    private List<Ue> ues;

    public Formation() {
        this.groupes = new ArrayList<>();
        this.ues = new ArrayList<>();
    }

    public int getTailleTP() {
        return tailleTP;
    }

    public int getId() {
        return id;
    }
    
    public void setTailleTP(int tailleTP) {
        this.tailleTP = tailleTP;
    }

    public int getTailleTD() {
        return tailleTD;
    }

    public void setTailleTD(int tailleTD) {
        this.tailleTD = tailleTD;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getnbUeOption() {
        return nbUeOption;
    }

    public void setnbUeOption(int nbUeOption) {
        this.nbUeOption = nbUeOption;
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
        if (this.ues.size() < this.nbUeOption) {
            this.ues.add(ue);
        } else {
            throw new IllegalArgumentException("The number of UEs cannot exceed the number of options.");
        }
    }
    
    
    public int getTailleTp() {
        return tailleTP;
    }

    public void setTailleTp(int tailleTp) {
        this.tailleTP = tailleTp;
    }
    
    public String getAnneeAcademique() {
        return annee_Academic;
    }

    public void setAnneeAcademique(String anneeAcademique) {
        this.annee_Academic = anneeAcademique;
    }
}
