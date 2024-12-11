package com.example.kitchenservicebackend.filter;

import com.example.kitchenservicebackend.constans.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JWTTokenValidatorFilter extends OncePerRequestFilter {



    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain)
            throws ServletException, IOException {
        String jwt = request.getHeader(SecurityConstants.JWT_HEADER); // Header "Authorization"
        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7); // Fjern "Bearer "
            try {
                SecretKey key = Keys.hmacShaKeyFor(jwt.getBytes(StandardCharsets.UTF_8));


                // Parse JWT-tokenet
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();

                // Hent brugernavn og roller fra tokenet
                String username = String.valueOf(claims.get("username"));
                String authorities = (String) claims.get("authorities");

                // Opret Authentication-objekt
                Authentication auth = new UsernamePasswordAuthenticationToken(
                        username, null, AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception e) {
                System.err.println("Fejl under validering af JWT: " + e.getMessage());
                throw new BadCredentialsException("Ugyldigt token modtaget!", e);
            }
        } else {
            System.out.println("JWT-header mangler eller er ugyldig.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // Returner 401 Unauthorized
            response.getWriter().write("JWT-header mangler eller er ugyldig");
            return;  // Stop videre behandling af requesten
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // Undgå validering for login og register
        return request.getServletPath().equals("/login") || request.getServletPath().equals("/register");
    }
}