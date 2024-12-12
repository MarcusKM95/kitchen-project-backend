package com.example.kitchenservicebackend.service;

import com.example.kitchenservicebackend.constans.SecurityConstants;
import org.springframework.stereotype.Service;

@Service
public class JWTService {

    private final SecurityConstants securityConstants;

    public JWTService(SecurityConstants securityConstants) {
        this.securityConstants = securityConstants;
    }

    public void exampleMethod() {
        String secret = securityConstants.getJwtSecret();
        long expiration = securityConstants.getJwtExpiration();
        System.out.println("Secret: " + secret);
        System.out.println("Expiration: " + expiration);
    }

    public String generateToken(String email) {

        return email;
    }
}
