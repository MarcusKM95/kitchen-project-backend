package com.example.kitchenservicebackend.constans;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class SecurityConstants {

   public static final String JWT_HEADER = "Authorization";
   public static final String JWT_PREFIX = "Bearer ";


   // Hent denne fra application.properties
   @Value("${jwt.secret}")
   private String jwtKey;

   // Hent expiration tid fra application.properties
   @Value("${jwt.expiration}")
   private long jwtExpiration;

}

