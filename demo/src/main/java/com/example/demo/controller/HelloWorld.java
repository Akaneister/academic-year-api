package com.example.demo.controller;

import com.example.demo.entities.Etudiant;
import com.example.demo.entities.Formation;
import com.example.demo.service.DatabaseService;
import com.example.demo.entities.Ue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.jdbc.core.JdbcTemplate;

@RestController
public class HelloWorld {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger logger = LoggerFactory.getLogger(HelloWorld.class);

    @Autowired
    private DatabaseService databaseService;

    @GetMapping
    public String sayHello() {
        return "Hello, World!";
    }
    //________________________________________________________Check base de donnée_____________________________________________________
    @GetMapping("/db-status")
    public String checkDatabaseConnection() {
        try {
            boolean isConnected = databaseService.isDatabaseConnected();
            if (isConnected) {
                logger.info("✅ Successfully connected to MySQL!"); // Log d'information pour une connexion réussie
                return "✅ Connected to MySQL!";
            } else {
                logger.error("❌ Error: Could not connect to MySQL!"); // Log d'erreur si la connexion échoue
                return "❌ Error: Could not connect to MySQL!";
            }
        } catch (Exception e) {
            // Log d'erreur détaillé si une exception est levée
            logger.error("Error occurred while checking MySQL connection", e);
            return "❌ Error: Could not connect to MySQL!";
        }
    }
    
    //________________________________________________________Annee-academique_____________________________________________________
    @GetMapping("/annees-academiques")
    public Map<String, Object> getAcademicYears(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int limite) {
        List<String> annees = databaseService.getAcademicYears(page, limite);
        int total = databaseService.getTotalAcademicYears();
        int pages = (int) Math.ceil((double) total / limite);

        Map<String, Object> response = new HashMap<>();
        response.put("annees", annees);
        response.put("total", total);
        response.put("pages", pages);

        return response;
    }



    @PostMapping("/annees-academiques")
    public ResponseEntity<String> addFormation(@RequestBody Formation formation) {
    try {
        // Tentative d'ajout de la formation dans la base de données
        databaseService.addFormation(
            formation.getNom(),
            formation.getTailleTP(),
            formation.getTailleTD(),
            formation.getnbUeOption(),
            formation.getAnneeAcademique()
        );

        logger.info("Formation added successfully: {}", formation.getNom()); // Log d'information
        return new ResponseEntity<>("Formation added successfully!", HttpStatus.CREATED);

    } catch (Exception e) {
        // Log d'erreur plus détaillé avec l'exception et les détails de la formation
        logger.error("Failed to add formation: {}. Error: {}", 
            formation != null ? formation.getNom() : "null", 
            e.getMessage(), 
            e
        );

        return new ResponseEntity<>("Failed to add formation. Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/annees-academiques/{id}")
    public ResponseEntity<Formation> getFormationById(@PathVariable Long id) {
        try {
            Formation formation = databaseService.getFormationById(id);
            if (formation != null) {
                return new ResponseEntity<>(formation, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Error retrieving formation with ID {}: {}", id, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/annees-academiques/{id}")
    public ResponseEntity<String> updateFormation(@PathVariable Long id, @RequestBody Formation formation) {
    try {
        boolean updated = databaseService.updateFormation(id, formation);
        if (updated) {
            logger.info("Formation with ID {} updated successfully", id);
            return new ResponseEntity<>("Formation updated successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Formation not found.", HttpStatus.NOT_FOUND);
        }
    } catch (Exception e) {
        logger.error("Failed to update formation with ID {}: {}", id, e.getMessage());
        return new ResponseEntity<>("Failed to update formation.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

@DeleteMapping("/annees-academiques/{id}")
public ResponseEntity<String> deleteFormation(@PathVariable Long id) {
    try {
        boolean deleted = databaseService.deleteFormation(id);
        if (deleted) {
            logger.info("Formation with ID {} deleted successfully", id);
            return new ResponseEntity<>("Formation deleted successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Formation not found.", HttpStatus.NOT_FOUND);
        }
    } catch (Exception e) {
        logger.error("Failed to delete formation with ID {}: {}", id, e.getMessage());
        return new ResponseEntity<>("Failed to delete formation.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

@GetMapping("/annees-academiques/{id}/etudiants")
public ResponseEntity<List<Etudiant>> getStudentsByFormationId(@PathVariable Long id) {
    try {
        List<Etudiant> students = databaseService.getStudentsByFormationId(id);
        if (students != null && !students.isEmpty()) {
            logger.info("Found {} students for formation ID {}", students.size(), id);
            return new ResponseEntity<>(students, HttpStatus.OK);
        } else {
            logger.warn("No students found for formation ID {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    } catch (Exception e) {
        logger.error("Failed to retrieve students for formation ID {}: {}", id, e.getMessage());
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

@GetMapping("/annees-academiques/{id}/groupes")
public ResponseEntity<List<String>> getGroupsByFormationId(@PathVariable Long id) {
    try {
        List<String> groups = databaseService.getGroupesByFormationId(id);
        if (groups != null && !groups.isEmpty()) {
            logger.info("Found {} groups for formation ID {}", groups.size(), id);
            return new ResponseEntity<>(groups, HttpStatus.OK);
        } else {
            logger.warn("No groups found for formation ID {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    } catch (Exception e) {
        logger.error("Failed to retrieve groups for formation ID {}: {}", id, e.getMessage());
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

@GetMapping("/annees-academiques/{id}/ues")
public ResponseEntity<List<Ue>> getUesByFormationId(@PathVariable Long id) {
    try {
        List<Ue> ues = databaseService.getUesByFormationId(id);
        if (ues != null && !ues.isEmpty()) {
            logger.info("Found {} UEs for formation ID {}", ues.size(), id);
            return new ResponseEntity<>(ues, HttpStatus.OK);
        } else {
            logger.warn("No UEs found for formation ID {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    } catch (Exception e) {
        logger.error("Failed to retrieve UEs for formation ID {}: {}", id, e.getMessage());
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

@PostMapping("/annees-academiques/{nomFormation}/inscrire/{numeroEtudiant}")
public ResponseEntity<String> inscrireEtudiant(@PathVariable String nomFormation, @PathVariable String numeroEtudiant) {
    try {
        // Appel de la méthode pour inscrire l'étudiant
        databaseService.inscrireEtudiant(nomFormation, numeroEtudiant); 
        
        // Si aucune exception n'est lancée, l'inscription a réussi
        logger.info("Student with number {} successfully enrolled in formation: {}", numeroEtudiant, nomFormation);
        return new ResponseEntity<>("Student enrolled successfully!", HttpStatus.OK);
    } catch (Exception e) {
        // Gestion des erreurs
        logger.error("Failed to enroll student with number {} in formation {}: {}", numeroEtudiant, nomFormation, e.getMessage());
        return new ResponseEntity<>("Failed to enroll student.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


@PutMapping("/annees-academiques/acceptation/{numeroEtudiant}")
public ResponseEntity<String> validerEtudiant(@PathVariable String numeroEtudiant) {
    try {
        // Requête SQL pour mettre à jour la validation de l'étudiant
        String sql = "UPDATE Etudiant SET validation = 1 WHERE numeroEtu = ?";
        
        // Met à jour l'étudiant avec le numéro donné
        int rowsAffected = jdbcTemplate.update(sql, numeroEtudiant);
        
        // Vérifier si l'étudiant a été trouvé et mis à jour
        if (rowsAffected > 0) {
            return new ResponseEntity<>("Student " + numeroEtudiant + " has been validated.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Student with number " + numeroEtudiant + " not found.", HttpStatus.NOT_FOUND);
        }
    } catch (Exception e) {
        // En cas d'erreur, log et renvoie une erreur interne
        logger.error("Failed to validate student: " + e.getMessage());
        return new ResponseEntity<>("Failed to validate student.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


@DeleteMapping("/annees-academiques/desinscription/{numeroEtudiant}")
public ResponseEntity<String> desinscrireEtudiant(@PathVariable String numeroEtudiant) {
    try {
        // Requête SQL pour supprimer l'étudiant de la formation
        String sql = "DELETE FROM Etudiant WHERE numeroEtu = ?";
        
        // Supprime l'étudiant avec le numéro donné
        int rowsAffected = jdbcTemplate.update(sql, numeroEtudiant);
        
        // Vérifier si l'étudiant a été trouvé et supprimé
        if (rowsAffected > 0) {
            return new ResponseEntity<>("Student " + numeroEtudiant + " has been unenrolled.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Student with number " + numeroEtudiant + " not found.", HttpStatus.NOT_FOUND);
        }
    } catch (Exception e) {
        // En cas d'erreur, log et renvoie une erreur interne
        logger.error("Failed to unenroll student: " + e.getMessage());
        return new ResponseEntity<>("Failed to unenroll student.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


//________________________________________________________Groupes_____________________________________________________


@GetMapping("/groupes")
public List<String> getGroupes() {
    return databaseService.getGroupes();
}

public String getMethodName(@RequestParam String param) {
    return new String();
}



}

    
   
    


    

