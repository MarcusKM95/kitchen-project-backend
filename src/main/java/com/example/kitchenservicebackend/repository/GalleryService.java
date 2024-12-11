package com.example.kitchenservicebackend.repository;

import com.example.kitchenservicebackend.model.Gallery;

import java.util.List;
import java.util.Optional;

public interface GalleryService {
    List<Gallery> getAllGalleries();
    Optional<Gallery> getGalleryById(Long id);
    Gallery saveGallery(Gallery gallery);
    void deleteGallery(Long id);
}
