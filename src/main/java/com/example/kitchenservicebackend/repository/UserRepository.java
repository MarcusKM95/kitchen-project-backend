package com.example.kitchenservicebackend.repository;

import com.example.kitchenservicebackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}

