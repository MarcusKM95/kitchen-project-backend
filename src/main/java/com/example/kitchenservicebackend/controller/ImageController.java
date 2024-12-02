package com.example.kitchenservicebackend.controller;

import com.example.kitchenservicebackend.dto.ImageResponseMessageDTO;
import com.example.kitchenservicebackend.model.Image;
import com.example.kitchenservicebackend.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            Image image = imageService.uploadImage(file);
            return ResponseEntity.ok().body(new ImageResponseMessageDTO("File uploaded successfully", image));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(new ImageResponseMessageDTO("Failed to upload file", null));
        }
    }
}
