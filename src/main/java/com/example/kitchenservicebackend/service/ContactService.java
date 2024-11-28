package com.example.kitchenservicebackend.service;

import com.example.kitchenservicebackend.model.Contact;
import com.example.kitchenservicebackend.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    // Find alle kontakter
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    // Opret en ny kontakt
    public Contact createContact(Contact contact) {
        return contactRepository.save(contact);
    }

    // Opdater en eksisterende kontakt
    public Contact updateContact(int id, Contact contactDetails) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found with id " + id));
        contact.setName(contactDetails.getName());
        contact.setEmail(contactDetails.getEmail());
        contact.setPhone(contactDetails.getPhone());
        contact.setZipcode(contactDetails.getZipcode()); // Husk nye felter som postnummer
        contact.setMessage(contactDetails.getMessage()); // Og besked
        return contactRepository.save(contact);
    }

    // Slet en kontakt
    public void deleteContact(int id) {
        contactRepository.deleteById(id);
    }

    // Find en enkelt kontakt (valgfrit)
    public Optional<Contact> getContactById(int id) {
        return contactRepository.findById(id);
    }
}
