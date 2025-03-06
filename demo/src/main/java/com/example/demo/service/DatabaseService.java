package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean isDatabaseConnected() {
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    
    public int getTotalAcademicYears() {
        String sql = "SELECT COUNT(DISTINCT annee_academique) FROM Formation";
        
        Integer total = jdbcTemplate.queryForObject(sql, Integer.class);
        return total != null ? total : 0;
    }

    public List<String> getAcademicYears(int page, int limite) {
        int offset = (page - 1) * limite;
        String sql = "SELECT DISTINCT annee_academique FROM Formation LIMIT ? OFFSET ?";
        
        // Utilise JdbcTemplate ou EntityManager pour exécuter la requête
        List<String> annees = jdbcTemplate.queryForList(sql, String.class, limite, offset);
    
        return annees;
    }

    public boolean checkIfAcademicYearExists(String anneeAcademique) {
        // Ici, tu vas vérifier si l'année académique existe déjà dans la base de données
        String query = "SELECT COUNT(*) FROM Formation WHERE annee_academique = ?";
        int count = jdbcTemplate.queryForObject(query, Integer.class, anneeAcademique);
        return count > 0; // Si count est supérieur à 0, l'année académique existe déjà
    }
    
    public void addAcademicYear(String anneeAcademique) {
        // Si l'année académique n'existe pas, on peut l'ajouter
        String insertQuery = "INSERT INTO Formation (nom, TailleTP, TailleTD, nbUeOption, annee_academique) VALUES (?, ?, ?, ?, ?)";
        
        // Exemple d'ajout avec des valeurs fictives, tu peux ajuster les valeurs en fonction de ce que tu veux
        jdbcTemplate.update(insertQuery, "Formation X", 10, 20, 4, anneeAcademique);
    }
    
}
