package com.example.kitchenservicebackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AboutUsController {

    @GetMapping("/api/about")
    public String getAboutUs() {
        return "About Us content from backend (optional)";
    }
}
