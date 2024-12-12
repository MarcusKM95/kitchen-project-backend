package com.example.kitchenservicebackend.repository;

import com.example.kitchenservicebackend.model.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GalleryRepository extends JpaRepository<Gallery, Long> {



    // You can add custom queries if needed
}

