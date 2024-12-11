package com.example.kitchenservicebackend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class GalleryService {

    @Value("${gallery.upload-dir}")
    private String uploadDir;

    // Upload billede
    public void uploadImage(MultipartFile file) throws IOException {
        // Kontroller, at filen ikke er tom
        if (file.isEmpty()) {
            throw new IOException("Filen er tom");
        }

        // Skab et sti til at gemme filen
        Path path = Paths.get(uploadDir + File.separator + file.getOriginalFilename());

        // Gem filen på den angivne sti
        Files.write(path, file.getBytes());
    }

    // Slet billede
    public boolean deleteImage(String filename) {
        Path path = Paths.get(uploadDir + File.separator + filename);
        try {
            return Files.deleteIfExists(path); // Returnerer true hvis filen blev slettet
        } catch (IOException e) {
            return false; // Hvis filen ikke kunne slettes
        }
    }

    // Hent alle billeder
    public String[] getAllImages() {
        File folder = new File(uploadDir);
        return folder.list((dir, name) -> name.endsWith(".jpg") || name.endsWith(".png")); // Eller andre formater
    }
}
