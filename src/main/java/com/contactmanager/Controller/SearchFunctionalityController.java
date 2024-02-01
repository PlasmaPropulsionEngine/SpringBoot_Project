package com.contactmanager.Controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.contactmanager.dao.ContactRepository;
import com.contactmanager.dao.UserRepository;
import com.contactmanager.model.Contact;
import com.contactmanager.model.User;

@RestController
public class SearchFunctionalityController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
//search 
@GetMapping("/search/{query}")	
public ResponseEntity<?>search(@PathVariable("query") String query,Principal principal)
{
	
	System.out.println(query);
	
	User userByEmail = this.userRepository.getUserByEmail(principal.getName());
	
	List<Contact> contacts = this.contactRepository.findByNameContainingAndUser(query,userByEmail);
	
	return ResponseEntity.ok(contacts);
	
	
}
	
	
	
	
	
	
	
}
