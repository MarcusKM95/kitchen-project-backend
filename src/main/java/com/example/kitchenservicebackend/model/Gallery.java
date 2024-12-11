package com.example.kitchenservicebackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "gallery")
public class Gallery {

    // Getters and Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = true)
    private String description;

    @Column(nullable = false)
    private String url;

    // Constructors
    public Gallery() {}

    public Gallery(String title, String description, String url) {
        this.title = title;
        this.description = description;
        this.url = url;
    }

}

