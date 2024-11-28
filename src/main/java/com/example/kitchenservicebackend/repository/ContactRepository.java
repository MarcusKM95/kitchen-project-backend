package com.example.kitchenservicebackend.repository;

import com.example.kitchenservicebackend.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
}
