package com.example.kitchenservicebackend.controller;

import com.example.kitchenservicebackend.model.Customer;
import com.example.kitchenservicebackend.repository.CustomerRepository;
import com.example.kitchenservicebackend.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController

@CrossOrigin(origins = "http://localhost:63342") // Tillad frontend-anmodninger
public class UserController {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JWTService jwtService;


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Customer customer) {
        ResponseEntity<String> response;
        try {
            String hashPwd = passwordEncoder.encode(customer.getPwd());
            customer.setPwd(hashPwd);


            if (customer.getRoles() == null || customer.getRoles().isEmpty()) {
                customer.setRoles("ROLE_USER");
            }

            Customer savedCustomer = customerRepository.save(customer);
            if (savedCustomer.getId() > 0) {
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


    @PostMapping("/dologin")
    public ResponseEntity<String> doLogin(@RequestBody Customer customer) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(customer.getEmail(), customer.getPwd()));

        if (authentication.isAuthenticated()) {
            String jwt = jwtService.generateToken(customer.getEmail()); // Brug JWTService til at generere token
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Authorization", "Bearer " + jwt)  // Returner JWT i headeren
                    .body("Welcome " + customer.getEmail() + ", you are successfully logged in");
        } else {
            throw new UsernameNotFoundException("Invalid user request..!!");
        }
    }

}


