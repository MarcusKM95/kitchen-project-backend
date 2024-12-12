package com.example.kitchenservicebackend.constans;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class SecurityConstants {

   // Konstant header-navn
   public static final String JWT_HEADER = "Authorization";

   // Dynamiske værdier fra application.properties
   @Value("${jwt.secret}")
   private String jwtSecret;

   @Value("${jwt.expiration}")
   private long jwtExpiration;
}
