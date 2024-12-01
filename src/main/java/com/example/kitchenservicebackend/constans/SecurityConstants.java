package com.example.kitchenservicebackend.constans;

import org.springframework.beans.factory.annotation.Value;

public final class SecurityConstants {

   // Konstanter, der bruges i JWT-token generering og validation
   public static final String JWT_HEADER = "Authorization";
   public static final String JWT_PREFIX = "Bearer ";

   // Hent denne fra application.properties
   @Value("${jwt.secret}")
   public static String JWT_KEY;

   // Forhindre instansiering af klassen
   private SecurityConstants() {
      throw new UnsupportedOperationException("Utility class should not be instantiated");
   }

   @Value("${jwt.expiration}")
   public static long JWT_EXPIRATION;
}
