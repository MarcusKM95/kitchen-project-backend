package com.example.kitchenservicebackend.service;

import com.example.kitchenservicebackend.repository.GalleryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class GalleryService {

    private final GalleryRepository galleryRepository;

    @Value("${gallery.upload-dir}")
    private String uploadDir;

    public GalleryService(GalleryRepository galleryRepository) {
        this.galleryRepository = galleryRepository;
    }

    // Upload billede
    public void uploadImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty");
        }

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath); // Create directory if it doesn't exist
        }

        Path path = uploadPath.resolve(Objects.requireNonNull(file.getOriginalFilename()));
        Files.write(path, file.getBytes());
        System.out.println("File uploaded: " + path.toAbsolutePath());
    }

    // Slet billede
    public boolean deleteImage(String filename) {
        Path path = Paths.get(uploadDir + File.separator + filename);
        try {
            if (!Files.exists(path)) {
                System.out.println("File not found: " + filename);
                return false;
            }
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Hent alle billeder
    public String[] getAllImages() {
        File folder = new File(uploadDir);
        if (!folder.exists() || !folder.isDirectory()) {
            return new String[0]; // Return empty array if directory doesn't exist
        }
        return folder.list((dir, name) -> name.matches(".*\\.(jpg|png|jpeg|gif)$"));
    }
}
