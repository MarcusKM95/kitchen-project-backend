package com.example.kitchenservicebackend.controller;

import com.example.kitchenservicebackend.model.User;
import com.example.kitchenservicebackend.repository.UserRepository;
import com.example.kitchenservicebackend.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTService jwtService;

    // Registreringsmetode
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        // Tjek om brugeren allerede eksisterer
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Email already in use");
        }

        // Krypter brugerens adgangskode
        String hashPwd = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPwd);

        // Hvis brugeren ikke har nogen rolle, sættes den til 'USER'
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(Collections.singletonList("ROLE_USER"));
        }

        try {
            // Gem brugeren
            User savedUser = userRepository.save(user);
            if (savedUser.getId() != null) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body("User successfully registered");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Registration failed");
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Exception occurred: " + ex.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> doLogin(@RequestBody User user) {

        Optional<User> existingUserOpt = userRepository.findByEmail(user.getEmail());

        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();

            if (passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {

                String jwt = jwtService.generateToken(existingUser.getEmail());

                return ResponseEntity.status(HttpStatus.OK)
                        .header("Authorization", "Bearer " + jwt)
                        .body("Welcome " + existingUser.getEmail() + ", you are successfully logged in");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid credentials");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid credentials");
        }
    }

}