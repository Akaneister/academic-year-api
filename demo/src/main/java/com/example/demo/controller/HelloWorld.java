package com.example.demo.controller;

import com.example.demo.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;


@RestController

public class HelloWorld {

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
    public ResponseEntity<String> createAcademicYear(@RequestBody String anneeAcademique) {
        try {
            boolean exists = databaseService.checkIfAcademicYearExists(anneeAcademique);
            if (exists) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Une année académique avec le même code existe déjà");
            }
            
            databaseService.addAcademicYear(anneeAcademique);
            return ResponseEntity.status(HttpStatus.CREATED).body("Année académique créée avec succès");
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur serveur interne");
        }
    }

    
}
