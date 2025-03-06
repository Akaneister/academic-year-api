package com.example.demo.controller;

import com.example.demo.entities.Formation;
import com.example.demo.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        if (databaseService.isDatabaseConnected()) {
            return "✅ Connected to MySQL!";
        } else {
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
            databaseService.addFormation(formation);
            logger.info("Formation added successfully: {}", formation.getNom()); // Log d'information
            return new ResponseEntity<>("Formation added successfully!", HttpStatus.CREATED);
        } catch (Exception e) {
            // Log d'erreur si l'ajout échoue
            logger.error("Failed to add formation: {}", formation.getNom(), e);
            return new ResponseEntity<>("Failed to add formation.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("annees-academiques/{id}")
    public Map<String, Object> getFormationById(@PathVariable("id") int id) {
        Formation formation = databaseService.getFormationById(id);
    
        Map<String, Object> response = new HashMap<>();
        if (formation != null) {
            response.put("formation", formation);
        } else {
            response.put("message", "Formation not found");
        }
    
        return response;
    }


    @PutMapping("/annees-academiques/{id}")
    public Map<String, Object> updateFormation(@PathVariable("id") int id, @RequestBody Formation formation) {
        Map<String, Object> response = new HashMap<>();

        // Vérifier si la formation existe déjà
        Formation existingFormation = databaseService.getFormationById(id);

        if (existingFormation != null) {
            // Si la formation existe, mettre à jour les informations
            databaseService.updateFormation(id, formation);
            response.put("message", "Formation updated successfully");
            response.put("formation", formation);
        } else {
            response.put("message", "Formation not found");
        }

        return response;
    }




   
    


    
}
