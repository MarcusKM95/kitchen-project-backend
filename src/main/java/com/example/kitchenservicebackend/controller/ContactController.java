package com.example.kitchenservicebackend.controller;

import com.example.kitchenservicebackend.model.Contact;
import com.example.kitchenservicebackend.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:63342")  // Matcher frontend URL
public class ContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping
    public List<Contact> getAllContacts() {
        return contactService.getAllContacts();
    }



    @PostMapping("/api/contacts")
    public ResponseEntity<String> createContact(@RequestBody Contact contact) {
        contactService.createContact(contact); // Gem kontakt i databasen

        // Returner bekræftelsesbesked som tekst
        return ResponseEntity.ok("Tak for din besked! Vi vil kontakte dig snart.");
    }

    @PutMapping("/{id}")
    public Contact updateContact(@PathVariable int id, @RequestBody Contact contactDetails) {
        return contactService.updateContact(id, contactDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteContact(@PathVariable int id) {
        contactService.deleteContact(id);
    }
}
