package com.contactmanager.Controller;

import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.contactmanager.dao.UserRepository;
import com.contactmanager.helper.Messages;
import com.contactmanager.model.Contact;
import com.contactmanager.model.User;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
public class HomeController {

	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	
	@Autowired	
	private UserRepository userrepository;
		
//home 	
@RequestMapping("/")
//@ResponseBody
public String homepage( Model model)
{

	model.addAttribute("title", "Home -Contact manager");
	return "home";
	
}

//about
@RequestMapping("/about")	
public String aboutpage(Model model)
{
	
	model.addAttribute("title", "About -Contact manager");
	
	return "about";
	
	
}

//signup page display
@RequestMapping("/signup")	
public String signpage(Model model)
{
	
	model.addAttribute("title", "Register -Contact manager");
	model.addAttribute("user",new User());
	return "signup";
		
}

//register user	
@RequestMapping(value="/register",method = RequestMethod.POST)
public String registeruser(@Valid  @ModelAttribute("user") User user, BindingResult result1 ,@RequestParam(value= "agree",defaultValue = "false") boolean agr,Model model ,HttpSession session)
{
	
	try
	{
		if(!agr)
		{
			System.out.println("you have not tick conditions");
			throw new Exception("you have not tick conditions");
		}
		
		if(result1.hasErrors())
		{
				System.out.println("error "+result1.toString());
			model.addAttribute("user",user);
			return "signup";
			
		}
		//normal user	
		user.setRole("ROLE_USER");
		user.setEnabled(true);
		user.setImageUrl("default.png");
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		System.out.println("agree "+ agr);
		System.out.println("user "+ user);
		
		User result = this.userrepository.save(user);
		
		model.addAttribute("user",new User());
		//here the  user  created class message is used  in that alert-sucess is bootstrap class is used
		session.setAttribute("message",new Messages("successfully registered","alert-success"));
		
		return "signup";
		
	}
	
	catch (Exception e) 
	{	e.printStackTrace();
		model.addAttribute("user",user);
		session.setAttribute("message",new Messages("somthing went wrong "+e.getMessage(),"alert-danger"));
		
		return "signup";
	}
	

	
}


@GetMapping("/signin")
public String customlogin(Model model)
{
	
	model.addAttribute("title","Login -Contact manager");
	
	System.out.println("user customlogin handler  active");
	return "login";
	
}
















}
