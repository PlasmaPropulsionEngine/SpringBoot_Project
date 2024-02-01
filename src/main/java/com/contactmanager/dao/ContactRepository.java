package com.contactmanager.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.contactmanager.model.Contact;
import com.contactmanager.model.User;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

	//pagination
	
	
//	@Query("from Contact c where c.user.id =:userid")
//	public List<Contact>findContactsByUser(@Param("userid")  int userid);
//	
	
	@Query("from Contact c where c.user.id =:userid")
	public Page<Contact>findContactsByUser(@Param("userid")  int userid, Pageable  pageable);
	
	
	//search 
	public List<Contact> findByNameContainingAndUser(String name,User user);
	
	
	
	
}
