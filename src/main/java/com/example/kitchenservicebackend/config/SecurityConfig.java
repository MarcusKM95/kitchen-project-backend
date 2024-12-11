package com.example.kitchenservicebackend.config;

import com.example.kitchenservicebackend.filter.JWTTokenGeneratorFilter;
import com.example.kitchenservicebackend.filter.JWTTokenValidatorFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName("_csrf");

        // Konfiguration for HTTP Security
        http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Ingen session
                .cors(corsCustomizer -> corsCustomizer.configurationSource(corsConfigurationSource())) // Aktiver CORS
                .csrf(csrf -> csrf
                        .csrfTokenRequestHandler(requestHandler)
                        .ignoringRequestMatchers("/api/contacts", "/register", "/login", "/admin/**") // Ignorer CSRF for specifikke ruter
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // Brug cookie-baseret CSRF
                )
                .addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class) // JWT-validering før BasicAuthentication
                .addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class) // JWT-token generering
                .authorizeRequests(auth -> auth
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Kun ADMIN kan tilgå /admin
                        .requestMatchers("/api/gallery/upload**","/api/contacts", "/notices", "/contact", "/register", "/login").permitAll() // Åben adgang til disse ruter
                        .anyRequest().authenticated() // Alle andre ruter kræver autentifikation
                )
                .httpBasic(Customizer.withDefaults()); // HTTP Basic Auth for standard autentifikation

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:63342", "http://example.com")); // Tillad disse oprindelser
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Tillad disse metoder
        configuration.setAllowedHeaders(Collections.singletonList("*")); // Tillad alle headers
        configuration.setExposedHeaders(Arrays.asList("Authorization")); // Eksponér Authorization header
        configuration.setAllowCredentials(true); // Tillad credentials (cookies, authorization headers)
        configuration.setMaxAge(3600L); // Angiv maxAge for CORS-politik

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Anvend CORS-konfiguration på alle endpoints
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder(); // Brug delegating password encoder til hashning
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager(); // Opret AuthenticationManager
    }
}
