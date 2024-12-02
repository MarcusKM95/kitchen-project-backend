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
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName("_csrf");

        http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless session management for JWT
                .cors(corsCustomizer -> corsCustomizer.configurationSource(corsConfigurationSource())) // CORS configuration
                .csrf(csrf -> csrf
                        .csrfTokenRequestHandler(requestHandler)
                        .ignoringRequestMatchers("/api/uploads/**", "/api/contacts", "/login", "/register", "/dologin", "/admin/**") // Ignore CSRF for these public endpoints
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // CSRF token handling for browsers
                )
                .addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class) // Add JWT Generator filter for token creation
                .addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class) // Add JWT Validator filter to validate the token
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Admin protected
                        .requestMatchers("/api/contacts", "/notices", "/contact", "/register", "/login", "/dologin", "/api/images/upload").permitAll() // Public endpoints, including image upload
                        .anyRequest().authenticated() // All other requests require authentication
                )
                .formLogin(Customizer.withDefaults()) // Enable form login if needed
                .httpBasic(Customizer.withDefaults()); // Enable basic authentication if needed

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:63342")); // Adjust for your frontend's URL
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Allow these HTTP methods
        configuration.setAllowedHeaders(Collections.singletonList("*")); // Allow all headers (you may want to limit this for security)
        configuration.setExposedHeaders(Arrays.asList("Authorization")); // Expose the Authorization header so the client can access the JWT token
        configuration.setAllowCredentials(true); // Allow credentials to be included
        configuration.setMaxAge(3600L); // Cache pre-flight requests for 1 hour

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply CORS configuration to all endpoints
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
