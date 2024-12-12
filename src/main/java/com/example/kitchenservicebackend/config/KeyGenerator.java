package com.example.kitchenservicebackend.config;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import java.util.Base64;

public class KeyGenerator {
    public static void main(String[] args) {
        // Генерация безопасного ключа для HMAC-SHA256 (256 бит)
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        // Кодирование ключа в Base64
        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());

        // Печать Base64-кодированного ключа
        System.out.println("Base64 encoded key: " + encodedKey);

        // Печать длины ключа
        System.out.println("Key length: " + key.getEncoded().length + " bytes");
    }
}
