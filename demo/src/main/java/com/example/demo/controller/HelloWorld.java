package com.example.demo.controller;

import com.example.demo.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
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
}
