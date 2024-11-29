package com.example.kitchenservicebackend.controller;

import com.example.kitchenservicebackend.model.User;  // Eller Customer hvis du bruger det
import com.example.kitchenservicebackend.repository.UserRepository;  // Eller CustomerRepository
import com.example.kitchenservicebackend.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;  // Eller CustomerRepository

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JWTService jwtService;

    // Registreringsmetode
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        ResponseEntity<String> response;
        try {
            String hashPwd = passwordEncoder.encode(user.getPassword());
            user.setPassword(hashPwd);

            if (user.getRoles() == null || user.getRoles().isEmpty()) {
                user.setRoles("ROLE_USER");
            }

            User savedUser = userRepository.save(user);
            if (savedUser.getId() > 0) {
                response = ResponseEntity.status(HttpStatus.CREATED)
                        .body("User successfully registered");
            } else {
                response = ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Registration failed");
            }
        } catch (Exception ex) {
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Exception occurred: " + ex.getMessage());
        }
        return response;
    }

    // Login metode
    @PostMapping("/login")
    public ResponseEntity<String> doLogin(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

        if (authentication.isAuthenticated()) {
            String jwt = jwtService.generateToken(user.getEmail()); // Generer JWT token
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Authorization", "Bearer " + jwt)  // Returner JWT token i header
                    .body("Welcome " + user.getEmail() + ", you are successfully logged in");
        } else {
            throw new UsernameNotFoundException("Invalid user request..!!");
        }
    }
}
