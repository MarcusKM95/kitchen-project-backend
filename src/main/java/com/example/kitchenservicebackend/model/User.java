package com.example.kitchenservicebackend.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.util.List;

@Setter
@Getter
@Entity

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    private String phone;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
