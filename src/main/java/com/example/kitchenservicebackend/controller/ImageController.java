package com.example.kitchenservicebackend.controller;

import com.example.kitchenservicebackend.dto.ImageResponseMessageDTO;
import com.example.kitchenservicebackend.model.Image;
import com.example.kitchenservicebackend.service.ImageService;
import com.example.kitchenservicebackend.service.JWTTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private JWTTokenService jwtTokenService;

    @Value("${file.upload-dir}")
    private String UPLOAD_DIR;


    @PostMapping("/upload")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String authHeader) {
        System.out.println("hej");
        System.out.println("hej w");
        try {
            // Valider JWT-tokenet
            if (!jwtTokenService.validateToken(authHeader)) {
                return ResponseEntity.status(401).body(new ImageResponseMessageDTO("Unauthorized", null));
            }

            Image image = imageService.uploadImage(file);
            return ResponseEntity.ok().body(new ImageResponseMessageDTO("File uploaded successfully", image));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(new ImageResponseMessageDTO("Failed to upload file", null));
        }
    }

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> downloadImage(@PathVariable String filename) {
        Path path = Path.of(UPLOAD_DIR).resolve(filename);
        try {
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists()) {
                return ResponseEntity.ok().body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
