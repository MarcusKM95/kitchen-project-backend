package com.example.kitchenservicebackend.service;


import com.example.kitchenservicebackend.model.Gallery;
import com.example.kitchenservicebackend.repository.GalleryRepository;
import com.example.kitchenservicebackend.repository.GalleryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
class GalleryServiceImpl implements GalleryService {

    private final GalleryRepository galleryRepository;

    public GalleryServiceImpl(GalleryRepository galleryRepository) {
        this.galleryRepository = galleryRepository;
    }

    @Override
    public List<Gallery> getAllGalleries() {
        return galleryRepository.findAll();
    }

    @Override
    public Optional<Gallery> getGalleryById(Long id) {
        return galleryRepository.findById(id);
    }

    @Override
    public Gallery saveGallery(Gallery gallery) {
        return null;
    }

    @Override
    public void deleteGallery(Long id) {
        galleryRepository.deleteById(id);
    }
}
