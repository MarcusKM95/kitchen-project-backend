package com.example.kitchenservicebackend.filter;

import com.example.kitchenservicebackend.constans.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JWTTokenValidatorFilter extends OncePerRequestFilter {

    private final SecurityConstants securityConstants;

    public JWTTokenValidatorFilter(SecurityConstants securityConstants) {
        this.securityConstants = securityConstants;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwt = request.getHeader(SecurityConstants.JWT_HEADER); // Authorization header

        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7); // Fjern "Bearer " fra token

            try {
                // Hent hemmeligheden via injected SecurityConstants
                SecretKey key = Keys.hmacShaKeyFor(securityConstants.getJwtSecret().getBytes(StandardCharsets.UTF_8));

                // Parse JWT
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();

                String username = claims.getSubject();
                String authorities = (String) claims.get("authorities");

                // Map authorities til GrantedAuthority
                List<GrantedAuthority> grantedAuthorities = Arrays.stream(authorities.split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                // Set authentication i SecurityContext
                Authentication auth = new UsernamePasswordAuthenticationToken(username, null, grantedAuthorities);
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (ExpiredJwtException e) {
                handleErrorResponse(response, "JWT token is expired", e);
                return;
            } catch (SignatureException | MalformedJwtException e) {
                handleErrorResponse(response, "Invalid token signature", e);
                return;
            } catch (Exception e) {
                handleErrorResponse(response, "Invalid token received", e);
                return;
            }
        } else {
            // Ryd SecurityContext hvis ingen JWT findes
            SecurityContextHolder.clearContext();
        }

        // Fortsæt filterkæden
        filterChain.doFilter(request, response);
    }

    private void handleErrorResponse(HttpServletResponse response, String message, Exception e) throws IOException {
        // Log fejlen for fejlfinding
        e.printStackTrace();

        // Sæt HTTP-status og returnér fejlbesked
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // Definer endpoints, der ikke kræver filter
        List<String> excludedEndpoints = Arrays.asList("/login", "/register");
        return excludedEndpoints.contains(request.getServletPath());
    }
}
