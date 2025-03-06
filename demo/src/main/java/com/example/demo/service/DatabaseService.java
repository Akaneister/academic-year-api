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

    public void addFormation(Formation formation) {
        String query = "INSERT INTO Formation (TailleTP, TailleTD, nom, nbUeOption, annee_academique) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(query, 
            formation.getTailleTP(), 
            formation.getTailleTD(),  
            formation.getNom(), 
            formation.getNombreOption(),  
            formation.getAnneeAcademique());
    }
    
    public Formation getFormationById(int id) {
        String query = "SELECT * FROM Formation WHERE id = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{id}, new BeanPropertyRowMapper<>(Formation.class));
    }

    public void updateFormation(int id, Formation formation) {
        String query = "UPDATE Formation SET TailleTP = ?, TailleTD = ?, nom = ?, nbUeOption = ?, annee_academique = ? WHERE id = ?";
        jdbcTemplate.update(query, 
            formation.getTailleTP(), 
            formation.getTailleTD(),  
            formation.getNom(), 
            formation.getNombreOption(),  
            formation.getAnneeAcademique(),
            id);  
    }


    public void deleteFormation(Long id) {
        String query = "DELETE FROM Formation WHERE id = ?";
        jdbcTemplate.update(query, id);
    }
    


    public void getEtudiantFormation(Long id) {
        String query = "SELECT * FROM Formation WHERE id = ?";
        jdbcTemplate.queryForObject(query, new Object[]{id}, new BeanPropertyRowMapper<>(Formation.class));
    }

    public List<Etudiant> getEtudiantsByFormationId(int formationId) {
        String query = "SELECT * FROM Etudiant WHERE id = ?";
    
        // Utilisation d'un RowMapper pour mapper les résultats à des objets Etudiant
        return jdbcTemplate.query(query, new Object[]{formationId}, (rs, rowNum) -> {
            Etudiant etudiant = new Etudiant();
            etudiant.setId(rs.getInt("id"));
            etudiant.setNumeroEtudiant(rs.getString("numeroEtu"));
            etudiant.setValidation(rs.getInt("validation"));
            etudiant.setNom(rs.getString("nom"));
            etudiant.setPrenom(rs.getString("prenom"));
            return etudiant;
        });
    }
    
}





