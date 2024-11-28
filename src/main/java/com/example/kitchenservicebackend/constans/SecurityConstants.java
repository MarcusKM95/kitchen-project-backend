package com.example.kitchenservicebackend.constans;

public class SecurityConstants {

   // JWT-relaterede konstanter
   public static final String JWT_HEADER = "Authorization";
   public static final String JWT_PREFIX = "Bearer ";

   // JWT-nøgle (hent fra miljø eller konfigurationsfil)
   public static final String JWT_KEY = "mysecurejwtsecretmysecurejwtsecret"; // Brug @Value i stedet for hardkodning i produktion!

   // Roller
   public static final String ROLE_ADMIN = "ROLE_ADMIN";
   public static final String ROLE_USER = "ROLE_USER";

   private SecurityConstants() {
      // For at forhindre instansiering af denne klasse
   }
}
