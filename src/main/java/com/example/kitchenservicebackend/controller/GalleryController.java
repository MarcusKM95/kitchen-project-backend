package com.example.kitchenservicebackend.controller;

import com.example.kitchenservicebackend.service.GalleryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    @DeleteMapping("/delete/{filename}")
    public ResponseEntity<String> deleteImage(@PathVariable String filename) {
        boolean deleted = galleryService.deleteImage(filename);
        if (deleted) {
            return new ResponseEntity<>("Billede slettet succesfuldt!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Billede ikke fundet", HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint til at hente liste over billeder
    @GetMapping("/images")
    public ResponseEntity<?> getImages() {
        return new ResponseEntity<>(galleryService.getAllImages(), HttpStatus.OK);
    }
}
