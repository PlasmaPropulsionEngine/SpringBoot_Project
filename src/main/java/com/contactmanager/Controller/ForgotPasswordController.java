package com.contactmanager.Controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.contactmanager.dao.UserRepository;
import com.contactmanager.helper.Messages;
import com.contactmanager.model.User;
import com.contactmanager.service.Emailservice;

import jakarta.servlet.http.HttpSession;



@Controller
public class ForgotPasswordController {

	@Autowired
	Emailservice emailservice;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	//forgot passsword handler
	@GetMapping("/forgotPassword")
	public String forgotPassword(Model m)
	{
		
		m.addAttribute("title","Forgot-Password?");	
		return "forgotpassword";

	}
	
	
//verifyEmailid and send otp to email
@PostMapping("/sendotp")	
public String verifyEmailid(@RequestParam("email") String email,Model model,HttpSession session)
{
	
	
	System.out.println(email);
	
	
	Random random=new Random(1000);
	  int min = 100000; // Smallest 6-digit number
      int max = 999999; // Largest 6-digit number
      int otp = random.nextInt(max - min + 1) + min;
	
	String subject="OTP";
	String message="<div style='border:1px solid;padding:20px'>"+
					"<h1>"+"your OTP is:"+"<b>"+otp+"</b>"+"</h1>"+"</div>";
	String to=email;
	
	
	boolean f=this.emailservice.sendEmail(message, subject, to);
	
	System.out.println(otp);
	if(f)
	{
		session.setAttribute("serverotp",otp);
		session.setAttribute("email",email);
	
		session.setAttribute("message",new Messages("OTP is sent on registered Email ID Please check","danger"));
		return "/verifyotp";		
	}	
	else
	{
		//else not then send error  response  
		
		session.setAttribute("message",new Messages("check your email id", "danger"));
		return "/forgotpassword";		
	}
	
}
	
	
	
// get otp from frontend and match server side otp	

@PostMapping("/verifyotp")
public String verifyOTP(@RequestParam("otp") int otp,HttpSession session)
{
	
	int Serverotp=(int) session.getAttribute("serverotp");
	String email=(String) session.getAttribute("email");
	
	if(Serverotp==otp)
	{
		
		User userByEmail = this.userRepository.getUserByEmail(email);
		
		if(userByEmail==null)
		{
			//if user not register or enter wrong email 
			session.setAttribute("message",new Messages("user does not exist ,please register", "danger"));
			
		}
		else
		{
			return "/password_change_form";
		}
		
		
	}
	
	
	else
	{
		session.setAttribute("message",new Messages("Wrong OTP please check email id","danger"));
		return "/verifyotp";
	
	}
	
	

	return "/verifyotp";
	
	
	
}
	
	


//change password handler
@PostMapping("/changepassword")
public String changePassword(@RequestParam("newpassword") String newPass,HttpSession session)
{
	String email=(String) session.getAttribute("email");
	
	System.out.println(newPass + ""+ email);
	
	User userByEmail = this.userRepository.getUserByEmail(email);

	userByEmail.setPassword(passwordEncoder.encode(newPass));
	
	this.userRepository.save(userByEmail);
	
	return "redirect:/signin?change=password changed successfully, login here";
	
}







	
	
	
}
