package com.example.kitchenservicebackend.repository;

import com.example.kitchenservicebackend.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByOrderByOrderIndexAsc();
}
