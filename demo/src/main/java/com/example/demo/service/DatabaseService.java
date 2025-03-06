package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Etudiant;
import com.example.demo.entities.Formation;

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


    //ACADEMIC YEARS __________________________________________________________
    public int getTotalAcademicYears() {
        String sql = "SELECT COUNT(DISTINCT annee_academique) FROM Formation";
        
        Integer total = jdbcTemplate.queryForObject(sql, Integer.class);
        return total != null ? total : 0;
    }

    public List<String> getAcademicYears(int page, int limite) {
        int offset = (page - 1) * limite;
        String sql = "SELECT DISTINCT annee_academique FROM Formation LIMIT ? OFFSET ?";
        
        List<String> annees = jdbcTemplate.queryForList(sql, String.class, limite, offset);
    
        return annees;
    }

    public boolean checkIfAcademicYearExists(String anneeAcademique) {
        String query = "SELECT COUNT(*) FROM Formation WHERE annee_academique = ?";
        int count = jdbcTemplate.queryForObject(query, Integer.class, anneeAcademique);
        return count > 0;
    }

    public void addFormation(String nom, int tailleTP, int tailleTD, int nbUeOption, String anneeAcademique) {
        // SQL pour insérer une nouvelle formation
        String sql = "INSERT INTO Formation (nom, TailleTP, TailleTD, nbUeOption, annee_academique) VALUES (?, ?, ?, ?, ?)";

        // Mise à jour de la base de données
        jdbcTemplate.update(sql, nom, tailleTP, tailleTD, nbUeOption, anneeAcademique);
    }

    public Formation getFormationById(Long id) {
        String sql = "SELECT * FROM Formation WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Formation.class), id);
    }
    

    public boolean updateFormation(Long id, Formation formation) {
        String sql = "UPDATE Formation SET nom = ?, TailleTP = ?, TailleTD = ?, nbUeOption = ?, annee_academique = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, formation.getNom(), formation.getTailleTP(), formation.getTailleTD(), formation.getnbUeOption(), formation.getAnneeAcademique(), id);
        
        return rowsAffected > 0;
    }
    
    
    //__________________________________________________________________________
}





