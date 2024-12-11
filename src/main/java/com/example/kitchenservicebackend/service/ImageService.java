package com.example.kitchenservicebackend.service;

import com.example.kitchenservicebackend.model.Image;
import com.example.kitchenservicebackend.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class ImageService {
    @Value("${file.upload-dir}")
    private String uploadDir;

    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public List<Image> getAllImages() {
        return imageRepository.findAllByOrderByOrderIndexAsc();
    }

    public Image uploadImage(MultipartFile file) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File destinationFile = new File(uploadDir + File.separator + fileName);
        file.transferTo(destinationFile);

        int orderIndex = (int) imageRepository.count();
        String url = "/uploads" + fileName;

        Image image = new Image(url, orderIndex, fileName);
        return imageRepository.save(image);
    }

    public void deleteImage(Long id) {
        Image image = imageRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid image ID"));
        File file = new File(uploadDir + File.separator + image.getFileName());
        if (file.exists()) {
            file.delete();
        }
        imageRepository.delete(image);
    }

    public void reorderImages(Long id, boolean moveUp) {
        List<Image> images = imageRepository.findAllByOrderByOrderIndexAsc();
        int index = -1;

        for (int i = 0; i < images.size(); i++) {
            if (images.get(i).getId().equals(id)) {
                index = i;
                break;
            }
        }

        if (index == -1 || (moveUp && index == 0) || (!moveUp && index == images.size() - 1)) {
            return; // No reordering needed
        }

        int swapIndex = moveUp ? index - 1 : index + 1;
        Image currentImage = images.get(index);
        Image swapImage = images.get(swapIndex);

        int tempOrder = currentImage.getOrderIndex();
        currentImage.setOrderIndex(swapImage.getOrderIndex());
        swapImage.setOrderIndex(tempOrder);

        imageRepository.save(currentImage);
        imageRepository.save(swapImage);
    }
}
