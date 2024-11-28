package com.example.kitchenservicebackend.controller;

import com.example.kitchenservicebackend.model.Contact;
import com.example.kitchenservicebackend.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api") // Base path for all endpoints in this controller
public class ContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping("/contacts")
    public List<Contact> getAllContacts() {
        return contactService.getAllContacts();
    }

    @PostMapping("/contacts")
    public ResponseEntity<String> createContact(@RequestBody Contact contact) {
        contactService.createContact(contact); // Save contact to database

        // Return confirmation message as text
        return ResponseEntity.ok("Tak for din besked! Vi vil kontakte dig snart.");
    }

    @PutMapping("/contacts/{id}")
    public Contact updateContact(@PathVariable int id, @RequestBody Contact contactDetails) {
        return contactService.updateContact(id, contactDetails);
    }

    @DeleteMapping("/contacts/{id}")
    public void deleteContact(@PathVariable int id) {
        contactService.deleteContact(id);
    }
}
