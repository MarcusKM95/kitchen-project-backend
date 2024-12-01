package com.example.kitchenservicebackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:63342", "http://example.com") // Tilladte domæner
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Tilladte metoder
                .allowedHeaders("*") // Tilladte headers
                .exposedHeaders("Authorization") // Headers eksponeret til klienten
                .allowCredentials(true) // Tillad cookies/credentials
                .maxAge(3600); // Cache CORS-svar i en time
    }
}
