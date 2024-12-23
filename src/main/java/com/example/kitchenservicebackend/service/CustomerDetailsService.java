package com.example.kitchenservicebackend.service;

import com.example.kitchenservicebackend.model.Customer;
import com.example.kitchenservicebackend.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerDetailsService implements UserDetailsService {
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Customer> customer = customerRepository.findByEmail(username);
        if (customer.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // Hvis du validerer password under login, skal du ikke validere det her.
        return new org.springframework.security.core.userdetails.User(
                customer.get().getEmail(),
                customer.get().getPwd(),
                AuthorityUtils.createAuthorityList("ROLE_USER")  // Brug "ROLE_USER" eller din ønskede rolle
        );
    }




}
