package com.contactmanager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.contactmanager.dao.UserRepository;
import com.contactmanager.model.User;

public class UserDetailsServicesImpl  implements UserDetailsService{

	@Autowired
	private UserRepository userrepository;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User userByEmail = userrepository.getUserByEmail(username);
		
		if(userByEmail==null)
		{
			throw  new UsernameNotFoundException("user not found!!");
			
		}
		
		CustomUserDetails customeuserdetails=new CustomUserDetails(userByEmail);
		

		return customeuserdetails;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
