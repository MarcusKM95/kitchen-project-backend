package com.example.kitchenservicebackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;
    private int orderIndex;

    @Column(nullable = false)
    private String fileName;

    public Image() {}

    public Image(String url, int orderIndex, String fileName) {
        this.url = url;
        this.orderIndex = orderIndex;
        this.fileName = fileName;
    }

}
