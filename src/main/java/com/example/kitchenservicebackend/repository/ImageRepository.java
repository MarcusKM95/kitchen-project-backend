package com.example.kitchenservicebackend.repository;

import com.example.kitchenservicebackend.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Integer> {
}
