package com.example.demo.entities;


public class Groupe {
    private String type; // Correspond au champ 'type' dans la base de données
    private String nom;  // Correspond au champ 'nom' dans la base de données
    private long id;
    



    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    // Getters et Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
