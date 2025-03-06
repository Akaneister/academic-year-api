package com.example.demo.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Etudiant;
import com.example.demo.entities.Formation;
import com.example.demo.entities.Groupe;
import com.example.demo.entities.Ue;

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

    public boolean deleteFormation(Long id) {
        String sql = "DELETE FROM Formation WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }
    
    
    public List<Etudiant> getStudentsByFormationId(Long formationId) {
        // Requête SQL pour obtenir les étudiants en fonction de leur formation
        String sql = "SELECT numeroEtu AS numeroEtudiant, validation, compte_id AS nom, formation_id AS id FROM Etudiant WHERE formation_id = ?";
        
        // Utilisation de BeanPropertyRowMapper pour effectuer le mappage automatique
        List<Etudiant> students = jdbcTemplate.query(
            sql, 
            new BeanPropertyRowMapper<>(Etudiant.class), 
            formationId
        );
    
        
        return students;
        }
        
        public List<String> getGroupesByFormationId(Long formationId) {
            String sql = """
                SELECT DISTINCT g.nom 
                FROM Groupe g
                JOIN Groupe_UE gu ON g.id = gu.groupe_id
                JOIN UE u ON gu.ue_id = u.id
                JOIN Formation f ON u.formation_id = f.id
                WHERE f.id = ?;
            """;
        
            return jdbcTemplate.queryForList(sql, String.class, formationId);
        }

        public List<Ue> getUesByFormationId(Long formationId) {
            String sql = """
                SELECT u.id, u.nom, u.estObligatoire, u.capacite 
                FROM UE u
                JOIN Formation f ON u.formation_id = f.id
                WHERE f.id = ?;
            """;
        
            return jdbcTemplate.query(sql, (rs, rowNum) -> {
                Ue ue = new Ue();
                ue.setId(rs.getLong("id"));
                ue.setNom(rs.getString("nom"));
                ue.setObligatoire(rs.getBoolean("estObligatoire"));
                ue.setCapacite(rs.getInt("capacite"));
                    return ue;
                }, formationId);
            }

            public void inscrireEtudiant(String nomFormation, String numeroEtudiant) {
                // Vérifier si la formation existe
                String formationSql = "SELECT id FROM Formation WHERE nom = ?";
                Long formationId = jdbcTemplate.queryForObject(formationSql, Long.class, nomFormation);
            
                if (formationId == null) {
                    throw new IllegalArgumentException("Formation with name " + nomFormation + " not found");
                }
            
                // Vérifier si l'étudiant existe déjà dans la formation
                String etudiantSql = "SELECT COUNT(*) FROM Etudiant WHERE numeroEtu = ? AND formation_id = ?";
                int count = jdbcTemplate.queryForObject(etudiantSql, Integer.class, numeroEtudiant, formationId);
            
                if (count > 0) {
                    throw new IllegalStateException("Student with number " + numeroEtudiant + " is already enrolled in this formation");
                }
            
                // Inscrire l'étudiant dans la base de données
                String insertSql = "INSERT INTO Etudiant (numeroEtu, validation, formation_id) VALUES (?, 0, ?)";
                jdbcTemplate.update(insertSql, numeroEtudiant, formationId);
            }
            
    //__________________________________________________________________________Groupe__________________________

    public List<String> getGroupes() {
        String sql = "SELECT DISTINCT nom FROM Groupe";
        return jdbcTemplate.queryForList(sql, String.class);
    }


    public boolean createGroupe(Groupe groupe) {
        String sql = "INSERT INTO Groupe (type, nom) VALUES (?, ?)"; // Utilise 'type' ici
        try {
            int rowsAffected = jdbcTemplate.update(sql, groupe.getType(), groupe.getNom()); // Utilise getType() ici
            return rowsAffected > 0;
        } catch (Exception e) {
            return false;
        }
    }
    
     public Groupe getGroupeById(Long id) {
        String sql = "SELECT id, type, nom FROM Groupe WHERE id = ?";
        
        try {
            // Utilisation de jdbcTemplate pour interroger la base de données
            return jdbcTemplate.queryForObject(
                sql,
                new Object[]{id},
                (rs, rowNum) -> {
                    // Création d'un objet Groupe à partir du résultat
                    Groupe groupe = new Groupe();
                    groupe.setId(rs.getLong("id"));
                    groupe.setType(rs.getString("type"));
                    groupe.setNom(rs.getString("nom"));
                    return groupe;
                });
        } catch (EmptyResultDataAccessException e) {
            // Si aucun résultat n'est trouvé, on renvoie null
            return null;
        }
    }

    public boolean updateGroupe(Long id, Groupe groupe) {
        String sql = "UPDATE Groupe SET type = ?, nom = ? WHERE id = ?";

        try {
            int rowsAffected = jdbcTemplate.update(sql, groupe.getType(), groupe.getNom(), id);
            return rowsAffected > 0;  // Si une ligne a été mise à jour, alors la mise à jour a réussi
        } catch (Exception e) {
            // Gestion des erreurs
            return false;
        }
    }

    public boolean deleteGroupe(Long id) {
        String sql = "DELETE FROM Groupe WHERE id = ?";

        try {
            int rowsAffected = jdbcTemplate.update(sql, id);
            return rowsAffected > 0;  // Si une ligne a été supprimée, alors la suppression a réussi
        } catch (Exception e) {
            // Gestion des erreurs
            return false;
        }
    }

//__________________________________________________________________UE_____________________________
}





