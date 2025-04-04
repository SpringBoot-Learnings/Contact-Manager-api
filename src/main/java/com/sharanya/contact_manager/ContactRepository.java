package com.sharanya.contact_manager;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ContactRepository extends JpaRepository<Contact, Integer>{
    
}
