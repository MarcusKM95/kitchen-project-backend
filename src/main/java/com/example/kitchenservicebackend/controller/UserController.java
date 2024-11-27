package com.example.kitchenservicebackend.controller;

import com.example.kitchenservicebackend.model.Customer;
import com.example.kitchenservicebackend.repository.CustomerRepository;
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
    CustomerRepository customerRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Customer customer) {
        Customer savedCustomer = null;
        ResponseEntity response = null;
        try {
            String hashPwd = passwordEncoder.encode(customer.getPwd());
            customer.setPwd(hashPwd);
            savedCustomer = customerRepository.save(customer);
            if (savedCustomer.getId() > 0) {
                response = ResponseEntity.status(HttpStatus.CREATED)
                        .body("Given user details are successfully registrered");
            }
        } catch (Exception ex) {
            response = ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An exception occured due to" + ex.getMessage());
        }
        return response;
    }

    @PostMapping("/dologin")
    public ResponseEntity<String> doLogin(@RequestBody Customer customer) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(customer.getEmail(), customer.getPwd()));
        if(authentication.isAuthenticated()){
            //return JwtResponseDTO.builder()
            //        .accessToken(jwtService.GenerateToken(authRequestDTO.getUsername()).build();
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Du er logget på");
        } else {
            throw new UsernameNotFoundException("invalid user request..!!");
        }
    }
}
