package com.example.kitchenservicebackend.service;

import com.example.kitchenservicebackend.constans.SecurityConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JWTService {

    private static final String SECRET_KEY = SecurityConstants.JWT_KEY; // Secret key, som du allerede har defineret i SecurityConstants.

    // Metode til at generere JWT-token
    public String generateToken(String username) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setIssuer("KitchenPro")
                .setSubject("JWT Token")
                .claim("username", username)  // Gemmer brugernavnet i tokenet
                .setIssuedAt(new Date())  // Token udstedt nu
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // Token udløber efter 24 timer (86400000 ms)
                .signWith(key, SignatureAlgorithm.HS256)  // Signerer tokenet med den hemmelige nøgle
                .compact();
    }

    // Metode til at validere og udtrække oplysninger fra JWT-token (hvis nødvendigt)
    public String validateTokenAndGetUsername(String token) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("username", String.class);  // Udtrækker brugernavnet fra tokenet
    }
}
