package com.example.kitchenservicebackend.service;

import com.example.kitchenservicebackend.model.Image;
import com.example.kitchenservicebackend.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class ImageService {

    private static final String UPLOAD_DIR = "uploads/";

    @Autowired
    private ImageRepository imageRepository;

    public Image uploadImage(MultipartFile file) throws IOException {
        // Tjek om filen er tom
        if (file.isEmpty()) {
            throw new IOException("No file uploaded");
        }

        // Opret filsti til at gemme billedet
        Path uploadPath = Path.of(UPLOAD_DIR + file.getOriginalFilename());

        // Opret mappen, hvis den ikke findes
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            directory.mkdir();
        }

        // Kopier filen til den ønskede sti
        Files.copy(file.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);

        // Opret Image objekt og gem det i databasen
        Image image = new Image();
        image.setFileName(file.getOriginalFilename());
        image.setFilePath(uploadPath.toString());
        image.setFileType(file.getContentType());
        image.setFileSize(file.getSize());

        return imageRepository.save(image);  // Gem metadata i databasen
    }
}
