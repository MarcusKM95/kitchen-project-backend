package com.example.kitchenservicebackend.dto;


import com.example.kitchenservicebackend.model.Image;

public class ImageResponseMessageDTO {
    private String message;
    private Image image;

    // Constructor, Getters and Setters
    public ImageResponseMessageDTO(String message, Image image) {
        this.message = message;
        this.image = image;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }


}
