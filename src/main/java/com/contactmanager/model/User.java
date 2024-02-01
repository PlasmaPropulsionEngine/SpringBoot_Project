package com.contactmanager.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="USER")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@NotBlank(message = "filed can not be empty !!")
	@Size(min=3,max = 12, message = "user name must be between 3 to 12 charaters !!")
	private String  name;
	
	@Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$",message = "Invalid Email !!" )
	@Column(unique =true)
	private String  email;
	
	@NotBlank(message = "filed can not be empty !!")
	private String  password;
	//@Pattern(regexp = "^(?=.*\\d)(?=.*[A-Z]).{3,8}$",message = "min 3 charater max 8 charater and 1 lower case alphabet and  1 Upper case alphabet")
	private String  role;
	
	@Column(length=500)
	private String  about;
	
	private String  imageUrl;
		
	private boolean enabled;
	
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "user")
	private List<Contact>contact=new ArrayList<>();
	
	
	
	public List<Contact> getContact() {
		return contact;
	}



	public void setContact(List<Contact> contact) {
		this.contact = contact;
	}



	public User() {
		super();
		// TODO Auto-generated constructor stub
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public String getRole() {
		return role;
	}



	public void setRole(String role) {
		this.role = role;
	}



	public String getAbout() {
		return about;
	}



	public void setAbout(String about) {
		this.about = about;
	}



	public String getImageUrl() {
		return imageUrl;
	}



	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}



	public boolean isEnabled() {
		return enabled;
	}



	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}



	public User(int id, String name, String email, String password, String role, String about, String imageUrl,
			boolean enabled) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
		this.about = about;
		this.imageUrl = imageUrl;
		this.enabled = enabled;
	}



	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", role=" + role
				+ ", about=" + about + ", imageUrl=" + imageUrl + ", enabled=" + enabled + "]";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
