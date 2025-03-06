package com.example.demo.controller;

import com.example.demo.entities.Etudiant;
import com.example.demo.entities.Formation;
import com.example.demo.service.DatabaseService;
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

@RestController
public class HelloWorld {

    private static final Logger logger = LoggerFactory.getLogger(HelloWorld.class);

    @Autowired
    private DatabaseService databaseService;

    @GetMapping
    public String sayHello() {
        return "Hello, World!";
    }

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
            formation.getNombreOption(),
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


    




}

    
   
    


    

