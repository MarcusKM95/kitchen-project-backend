package com.example.kitchenservicebackend.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JWTTokenService {

    @Value("${jwt.secret}")
    private String secretKey;

    // Validerer JWT-tokenet
    public boolean validateToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
                return true; // Token er gyldig
            } catch (SignatureException e) {
                return false; // Token er ugyldigt
            }
        }
        return false; // Hvis der ikke er noget token eller det ikke starter med 'Bearer'
    }
}
