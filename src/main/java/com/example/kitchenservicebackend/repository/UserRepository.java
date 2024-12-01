package com.example.kitchenservicebackend.repository;

import com.example.kitchenservicebackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Find en bruger baseret på email
    Optional<User> findByEmail(String email);


    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

}


