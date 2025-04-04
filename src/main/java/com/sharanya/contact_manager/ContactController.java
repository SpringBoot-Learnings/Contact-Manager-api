package com.sharanya.contact_manager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {
    
    @Autowired
    private final ContactRepository contactRepository;
    public ContactController(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @PostMapping
    public List<Contact> addMultipleContacts(@RequestBody List<Contact> contacts) {
        return contactRepository.saveAll(contacts);
    }
    // Add a new contact
    @PostMapping("/add")
    public Contact addContact(@RequestBody Contact contact) {
        return contactRepository.save(contact);
    }

    // Get all contacts
    @GetMapping("/all")
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }
    // Update Contact By ID
    @PutMapping("/{id}")
public ResponseEntity<Contact> updateContactById(@PathVariable int id, @RequestBody Contact updatedContact) {
    Optional<Contact> existingContact = contactRepository.findById(id);

    if (existingContact.isPresent()) {
        Contact contact = existingContact.get();
        contact.setName(updatedContact.getName());
        contact.setEmail(updatedContact.getEmail());
        contact.setPhone(updatedContact.getPhone());
        contactRepository.save(contact);
        return ResponseEntity.ok(contact);
    } else {
        return ResponseEntity.notFound().build();
    }
}
    //Bulk Contact Update
    @PutMapping("/bulk-update")
    public ResponseEntity<List<Contact>> updateMultipleContacts(@RequestBody List<Contact> updatedContacts) {
        List<Contact> updatedList = new ArrayList<>();

        for (Contact updatedContact : updatedContacts) {
            Optional<Contact> existingContact = contactRepository.findById(updatedContact.getId());

            if (existingContact.isPresent()) {
                Contact contact = existingContact.get();
                contact.setName(updatedContact.getName());
                contact.setEmail(updatedContact.getEmail());
                contact.setPhone(updatedContact.getPhone());

                updatedList.add(contact);
            }
        }

        if (!updatedList.isEmpty()) {
            contactRepository.saveAll(updatedList);
            return ResponseEntity.ok(updatedList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    // Get a single contact by ID
    @GetMapping("/{id}")
    public Optional<Contact> getContactById(@PathVariable int id) {
        return contactRepository.findById(id);
    }

    // Delete a contact by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteContactById(@PathVariable int id) {
        if (contactRepository.existsById(id)) {
            contactRepository.deleteById(id);
            return ResponseEntity.ok("Contact deleted successfully!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    //Bulk Contact Delete
     @DeleteMapping("/bulk-delete")
    public ResponseEntity<String> deleteMultipleContacts(@RequestBody List<Integer> ids) {
        List<Contact> contactsToDelete = contactRepository.findAllById(ids);

        if (!contactsToDelete.isEmpty()) {
            contactRepository.deleteAll(contactsToDelete);
            return ResponseEntity.ok("Contacts deleted successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No contacts found for given IDs");
        }
    }
}
