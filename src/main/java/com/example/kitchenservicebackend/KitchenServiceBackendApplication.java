package com.example.kitchenservicebackend;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.crypto.SecretKey;

@SpringBootApplication
public class KitchenServiceBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(KitchenServiceBackendApplication.class, args);
    }

    SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    String base64Key = java.util.Base64.getEncoder().encodeToString(secretKey.getEncoded());




}
