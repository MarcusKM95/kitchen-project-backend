package com.example.kitchenservicebackend.filter;

import com.example.kitchenservicebackend.config.SecurityConfig;
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

    private SecurityConfig securityConfig;

    public JWTTokenValidatorFilter() {
        this.securityConfig = securityConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwt = request.getHeader("Authorization"); // Проверка на заголовок Authorization
        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7); // Убираем "Bearer "
            try {
                SecretKey key = Keys.hmacShaKeyFor(
                        SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));

                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();

                String username = String.valueOf(claims.get("username"));
                String authorities = (String) claims.get("authorities");

                Authentication auth = new UsernamePasswordAuthenticationToken(username, null,
                        AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception e) {
                System.err.println("JWT parsing failed: " + e.getMessage());
                throw new BadCredentialsException("Invalid Token received!", e);
            }
        } else {
            System.out.println("JWT header is missing or malformed.");
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        //if dologin, så ikke kør validate. ellers kør validate. (hvad med register)
        return request.getServletPath().equals("/dologin") || request.getServletPath().equals("/register");
    }
}
