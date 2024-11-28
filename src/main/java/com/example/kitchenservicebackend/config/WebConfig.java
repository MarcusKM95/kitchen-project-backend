package com.example.kitchenservicebackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Tillad alle endepunkter
                .allowedOrigins("http://localhost:63342") // Tillad din frontend-URL
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Tillad specifikke HTTP-metoder
                .allowedHeaders("*") // Tillad alle headers
                .allowCredentials(true); // Tillad cookies, hvis nødvendigt
    }
}
