package com.example.kitchenservicebackend.filter;

import com.example.kitchenservicebackend.constans.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwt = request.getHeader(SecurityConstants.JWT_HEADER); // Header "Authorization"

        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7); // Fjern "Bearer "

            try {
                // Hent hemmelig nøgle fra konstanten
                SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));

                // Parse JWT-tokenet
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();

                // Hent brugernavn og roller fra tokenet
                String username = claims.getSubject(); // Brug subject som brugernavn
                String authorities = (String) claims.get("authorities"); // Roller i kommasepareret streng

                // Opret Authentication-objekt
                Authentication auth = new UsernamePasswordAuthenticationToken(
                        username, null, AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));

                // Sæt Authentication i SecurityContext
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception e) {
                System.err.println("Fejl under validering af JWT: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Returner Unauthorized
                response.getWriter().write("Ugyldigt token");
                return; // Stop videre behandling, hvis JWT er ugyldigt
            }
        } else {
            System.out.println("JWT-header mangler eller er ugyldig.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Returner Unauthorized
            response.getWriter().write("JWT header mangler eller er ugyldig");
            return;
        }

        filterChain.doFilter(request, response); // Fortsæt filterkæden
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // Undgå validering for login og register
        String path = request.getServletPath();
        return path.equals("/dologin") || path.equals("/register") || path.equals("/login") || path.equals("/dologin");
    }
}
