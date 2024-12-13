package com.example.kitchenservicebackend.service;

import com.example.kitchenservicebackend.model.Gallery;
import com.example.kitchenservicebackend.repository.GalleryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GalleryService {

    private final GalleryRepository galleryRepository;

    @Value("${gallery.upload-dir}")
    private String uploadDir;

    public GalleryService(GalleryRepository galleryRepository) {
        this.galleryRepository = galleryRepository;
    }

    // Upload billede
    // Upload billede
    public void uploadImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty");
        }

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath); // Create directory if it doesn't exist
        }

        String fileName = Objects.requireNonNull(file.getOriginalFilename());
        Path path = uploadPath.resolve(fileName);
        Files.write(path, file.getBytes());
        System.out.println("File uploaded: " + path.toAbsolutePath());

        // Save image metadata to database
        Gallery gallery = new Gallery(fileName); // Assuming 'name' is used to store the file name
        galleryRepository.save(gallery);
        System.out.println("Image metadata saved to database: " + fileName);
    }

    public Optional<Gallery> getImageById(Long id) {
        return galleryRepository.findById(id);
    }


    // Slet billede
    public void deleteImage(Long id) {
        Optional<Gallery> gallery = galleryRepository.findById(id);

        if (gallery.isPresent()) {
            // Delete file from file system
            String fileName = gallery.get().getName();
            Path path = Paths.get(uploadDir, fileName);

            try {
                if (Files.exists(path)) {
                    Files.delete(path); // Delete file from storage
                    System.out.println("File deleted: " + path.toAbsolutePath());
                } else {
                    System.out.println("File not found: " + fileName);
                }
            } catch (IOException e) {
                throw new RuntimeException("Error deleting file: " + fileName, e);
            }

            // Delete database entry
            galleryRepository.deleteById(id);
            System.out.println("Image metadata deleted from database: " + id);
        } else {
            throw new IllegalArgumentException("Image with ID " + id + " not found in database");
        }
    }


    // Hent alle billeder
    public List<String> getAllImages() {
        return galleryRepository.findAll()
                .stream()
                .map(Gallery::getName)
                .collect(Collectors.toList());
    }

}
