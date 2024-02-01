package com.contactmanager.Controller;

import java.io.File;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.contactmanager.dao.ContactRepository;
import com.contactmanager.dao.UserRepository;
import com.contactmanager.helper.Messages;
import com.contactmanager.model.Contact;
import com.contactmanager.model.User;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserRepository userrepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private ContactRepository contactRepository;
	//common data 
	@ModelAttribute
	public void addcommonData(Model model,Principal principle)
	{
		
		String username = principle.getName();
		
		System.out.println("username: "+ username);
		
		//get the username(email)
		User userByEmail = this.userrepository.getUserByEmail(username);

		System.out.println("user "+ userByEmail);
		model.addAttribute("user",userByEmail);
		
	}
	
	
	//dashboard home user
	@RequestMapping("/index")
	public String dashboard( Model model,Principal principle)
	{
		

		model.addAttribute("title","User-home");
		
		return "normal/dashboard";
		
	}
	
	
	
	//add contacts handler
	@GetMapping("/addcontact")
	public String AddContact(Model model)
	{
		
		model.addAttribute("title","Add-Contact");
		model.addAttribute("contact",new Contact());
		
		return "normal/addContactForm";
	}
	
	
	//save contact
	@PostMapping("/process-contact")
	public String processContact( @ModelAttribute Contact contact
			,@RequestParam("img") MultipartFile  file 
			, Principal principal,HttpSession session)
	{
		try
		{
		String name = principal.getName();
		//username=user email
		User user=this.userrepository.getUserByEmail(name);
	
		
		//prossesing file
		if(file.isEmpty())
		{
			System.out.println("file is emapty");
			contact.setImage("profile.png");
		}
		else
		{
			//upload file to folder and then save the name in database
			contact.setImage(file.getOriginalFilename());
			System.out.println(new ClassPathResource("static/image").getFile().getAbsolutePath());
			File savefile=new ClassPathResource("static/image").getFile().getAbsoluteFile();
			Path path = Paths.get(savefile+File.separator+file.getOriginalFilename());
			Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
			System.out.println("image uploaded");
			System.out.println(contact.getImage());
			

		}
		
		
		
		contact.setUser(user);
		
		user.getContact().add(contact);
		
		this.userrepository.save(user);
		System.out.println("data contact" + contact);
		System.out.println("data added");
		session.setAttribute("message",new Messages("Contact added successfully","success"));
		
		}
		catch( Exception e)
		{
			e.printStackTrace();
			session.setAttribute("message",new Messages("somthing went wrong try again","danger"));
		}
		return "normal/addContactForm";
		
	}
	
	
//show contacts per page 
//contact 5 per page
//page 0 index	
	
@GetMapping("/show-contacts/{page}")	
public String showContacts(@PathVariable("page") Integer page ,Model model,Principal principal)
{
	//here username=emailid
	
	model.addAttribute("title","Show-Contact");
	
	String Username = principal.getName();
//	
	User userByEmail = this.userrepository.getUserByEmail(Username);
//	

//	List<Contact> contact = userByEmail.getContact();

	Pageable pageable =PageRequest.of(page, 5);
	
	Page<Contact> contacts = this.contactRepository.findContactsByUser(userByEmail.getId(), pageable);
	model.addAttribute("contacts",contacts);
	model.addAttribute("currentpage",page);
	model.addAttribute("totalpages",contacts.getTotalPages());
	
	
	
return "normal/viewcontact";	
	
	
}
	
	
//show specific single contact 	
@RequestMapping("/{cId}/contact")	
public String showContactDetail(@PathVariable("cId") Integer cid,Model model,Principal principal)
{
	
	System.out.println(cid);
	
	Optional<Contact> optionalcontact = this.contactRepository.findById(cid);
	
	Contact contact = optionalcontact.get();
	
	String username = principal.getName();
	
	User userByEmail = this.userrepository.getUserByEmail(username);
	
	if(userByEmail.getId()==contact.getUser().getId())
	{
		model.addAttribute("contact",contact);
	}
	
	return "normal/contactdetail";
	
	
	
}
	

//delete contact handler
@GetMapping("/delete/{cid}")
public String deleteContact(@PathVariable("cid") Integer cid,Model model,Principal principal,HttpSession session)
{
	
	Optional<Contact> optionalContact = this.contactRepository.findById(cid);
	Contact contact = optionalContact.get();
	
	String username = principal.getName();
	
	User userByEmail = this.userrepository.getUserByEmail(username);
	
	if(userByEmail.getId()==contact.getUser().getId())
	{
	//user table and contact table linked cascade to unlink contact by contact id set user null
		contact.setUser(null);
	this.contactRepository.delete(contact);
	
	session.setAttribute("message",new Messages("contact deleted successfully..","success"));
	}
	
	
	
	return "redirect:/user/show-contacts/0";
	
	
}


// open update  contact form 
@PostMapping("/updatecontact/{cid}")
public String updateForm(@PathVariable("cid") Integer cid,Model model)
{
	
	model.addAttribute("title","Update-Contact");
	
	Contact contact = this.contactRepository.findById(cid).get();
	
	model.addAttribute("contact", contact);
	
	return "normal/updateform";
	
}
	
	
//process update contact
@RequestMapping(value="/process-update",method = RequestMethod.POST)
public String updateContact(@ModelAttribute Contact contact,@RequestParam("img") MultipartFile file,Model model,
		HttpSession session,Principal principal)
{
		
	
	try {
		
		//old contact details
		Contact oldcontact = this.contactRepository.findById(contact.getcId()).get();
		
		System.out.println("old contact "+oldcontact);
		
		//new image
		if(!file.isEmpty())
		{
			
			//delete old image
			
			File deletefile=new ClassPathResource("static/image").getFile();
			
			File deletewithname=new File(deletefile,oldcontact.getImage());
			deletewithname.delete();
			
			
			//update new image
			File savefile=new ClassPathResource("static/image").getFile().getAbsoluteFile();
			Path path = Paths.get(savefile+File.separator+file.getOriginalFilename());
			Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
			
			contact.setImage(file.getOriginalFilename());
	
		}
		
		else
		{
			//if is not selected then set the old one
			contact.setImage(oldcontact.getImage());
		}
		
		User user=this.userrepository.getUserByEmail(principal.getName());
		
		contact.setUser(user);
	
		System.out.println("user :"+user);
		this.contactRepository.save(contact);
			
		session.setAttribute("message",new Messages("contact is updated..","success"));
		
	}
	catch (Exception e) {
		e.printStackTrace();
		session.setAttribute("message",new Messages("somthing went wrong try again..","danger"));
	}
	
	
	
	
	System.out.println("contact name "+contact.getName());
	System.out.println("contact ID "+contact.getcId());
	
	return "redirect:/user/"+contact.getcId()+"/contact";
}
	

//your profile handler

@GetMapping("/yourprofile")
public String yourProfile(Model model)
{
	
	model.addAttribute("title","Profile");
	
	
 return "normal/profile";		
}


//setting handler
@GetMapping("/settings")
public String openSettings()
{
		
	return "normal/settings";
		
}


//change password handler
@PostMapping("/change-password")
public String changePassword(@RequestParam("old-password") String oldpass, @RequestParam("new-password") String newdpass,Principal principal,HttpSession session)
{
	System.out.println("old password: "+oldpass +"and new pass "+ newdpass);
	
	
	String username = principal.getName();
	
	User userByEmail = this.userrepository.getUserByEmail(username);
	
	String password = userByEmail.getPassword();
	
	
	if(this.bCryptPasswordEncoder.matches(oldpass,password))
	{
		//change pass
		
		
		userByEmail.setPassword(this.bCryptPasswordEncoder.encode(newdpass));
		this.userrepository.save(userByEmail);
		session.setAttribute("message",new Messages("Password changed successfully..","success"));
	}
	else
	{
		
		//give the error message
		session.setAttribute("message",new Messages("Please enter correct old password","danger"));
		return "redirect:/user/settings";
	}
	
	System.out.println( "encrpted password "+password);
	
	return "redirect:/user/index";
	
	
}





	
	
}
