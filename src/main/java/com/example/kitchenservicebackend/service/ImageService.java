package com.example.kitchenservicebackend.service;

import com.example.kitchenservicebackend.model.Image;
import com.example.kitchenservicebackend.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${file.upload-dir}")
    private String UPLOAD_DIR;

    @Autowired
    private ImageRepository imageRepository;

    public Image uploadImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("No file uploaded");
        }

        String uniqueFileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        Path uploadPath = Path.of(UPLOAD_DIR + uniqueFileName);

        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        Files.copy(file.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);

        Image image = new Image();
        image.setFileName(uniqueFileName);
        image.setFilePath(uploadPath.getFileName().toString()); // Gem kun filnavnet
        image.setFileType(file.getContentType());
        image.setFileSize(file.getSize());

        return imageRepository.save(image);
    }
}
