package com.example.demo;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Permet toutes les origines à accéder aux ressources de l'API
        registry.addMapping("/**")  // Applique la configuration CORS à toutes les routes de l'API
                .allowedOrigins("http://localhost:8080")  // Remplace par l'URL de ton frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*");
    }
}