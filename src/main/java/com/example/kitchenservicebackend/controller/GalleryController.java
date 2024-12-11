package com.example.kitchenservicebackend.controller;

import com.example.kitchenservicebackend.model.Gallery;
import com.example.kitchenservicebackend.repository.GalleryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/gallery")
public class GalleryController {

    @Autowired
    private GalleryRepository galleryRepository;

    // Hent alle billeder
    @GetMapping
    public List<Gallery> getAllImages() {
        return galleryRepository.findAll();
    }

    // Hent et billede baseret på ID
    @GetMapping("/{id}")
    public ResponseEntity<Gallery> getImageById(@PathVariable Long id) {
        Optional<Gallery> gallery = galleryRepository.findById(id);
        return gallery.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Opret et nyt billede
    @PostMapping
    public ResponseEntity<Gallery> createImage(@RequestBody Gallery gallery) {
        Gallery savedGallery = galleryRepository.save(gallery);
        return ResponseEntity.status(201).body(savedGallery);
    }

    // Opdater et billede
    @PutMapping("/{id}")
    public ResponseEntity<Gallery> updateImage(
            @PathVariable Long id,
            @RequestBody Gallery updatedGallery) {
        return galleryRepository.findById(id).map(existingGallery -> {
            existingGallery.setTitle(updatedGallery.getTitle());
            existingGallery.setDescription(updatedGallery.getDescription());
            existingGallery.setUrl(updatedGallery.getUrl());
            galleryRepository.save(existingGallery);
            return ResponseEntity.ok(existingGallery);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Slet et billede
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        if (galleryRepository.existsById(id)) {
            galleryRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
