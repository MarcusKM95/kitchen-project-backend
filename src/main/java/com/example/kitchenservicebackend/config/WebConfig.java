package com.example.kitchenservicebackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Applies to all endpoints
                .allowedOrigins("http://localhost:63342", "http://example.com") // Allowed origins
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed methods
                .allowedHeaders("*") // Allow all headers from the client
                .exposedHeaders("Authorization") // Expose the Authorization header to the client
                .allowCredentials(true) // Allow cookies/credentials
                .maxAge(3600); // Cache CORS response for 1 hour
    }
}
