package com.example.kitchenservicebackend.controller;

import com.example.kitchenservicebackend.model.Gallery;
import com.example.kitchenservicebackend.service.GalleryService;
import jakarta.annotation.Resource;
import jakarta.persistence.criteria.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/gallery")
public class GalleryController {

    @Autowired
    private GalleryService galleryService;

    // Endpoint til at uploade billede
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            galleryService.uploadImage(file);
            return new ResponseEntity<>("Billede uploadet succesfuldt!", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Fejl under upload af billede", HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    // Endpoint til at slette billede
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteImage(@PathVariable Long id) {
        try {
            galleryService.deleteImage(id);
            return new ResponseEntity<>("Billede slettet succesfuldt!", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Fejl under sletning af billede", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<?> getImageById(@PathVariable Long id) {
        Optional<Gallery> gallery = galleryService.getImageById(id);
        if (gallery.isPresent()) {
            return new ResponseEntity<>(gallery.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Image not found", HttpStatus.NOT_FOUND);
        }
    }


   @GetMapping("/images")
    public ResponseEntity<?> getImages(@RequestHeader("Authorization") String authHeader) {
        // Assuming JWT token validation logic
        if (isValidToken(authHeader)) {
            return new ResponseEntity<>(galleryService.getAllImages(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }

    private boolean isValidToken(String authHeader) {
        return authHeader != null && authHeader.startsWith("Bearer ");
    }






}
