package com.example.kitchenservicebackend.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTService {

    @Value("${jwt.secret}") // Denne annotation binder værdien fra application.properties eller application.yml
    private String SECRET_KEY;

    private final long JWT_EXPIRATION_TIME = 3600000;  // 1 time
    private final long JWT_REFRESH_EXPIRATION_TIME = 86400000;  // 24 timer, for eksempel for admin-brugere



    @Value("${jwt.secret}")
    private String secretKey;

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }



    // Kontroller om tokenet er udløbet
    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    // Hent brugernavn fra token
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
