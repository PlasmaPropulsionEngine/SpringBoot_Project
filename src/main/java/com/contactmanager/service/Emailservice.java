package com.contactmanager.service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class Emailservice {

	
	public static boolean sendEmail(String message, String subject, String to) {

		//variable gmail host
		  String from="example@...."; //give here email service from
		  String host="smtp.gmail.com";
		
		boolean f=false;
		
		//get the system properties
		
		//setting important information  to properties  object
		//host
		
		Properties properties = System.getProperties();
		
		System.out.println(properties);
		
		properties.put("mail.smtp.host", host);
		
		properties.put("mail.smtp.port", "465");

		properties.put("mail.smtp.ssl.enable", "true");
		
		properties.put("mail.smtp.auth", "true");
		
		
		//step1: to get the session object
			
		Session session=Session.getInstance(properties, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("give here email (from)","here password");
			}			
			
		});

		session.setDebug(true);
		
		//step :2compose the message[text,attachment]
		
		MimeMessage mimeMessage = new MimeMessage(session);
		
		try
		{
			//from email

		mimeMessage.setFrom(from);
		 //adding recipient to message
		mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		
		//adding subject to message
		mimeMessage.setSubject(subject);
		
		//adding message to email
		
		//mimeMessage.setText(message);
		
		mimeMessage.setContent(message,"text/html");
		
		//send
		
		//send using transport class
		Transport.send(mimeMessage);
		
		System.out.println("done ");
		
		}
		catch (Exception e) {
			
			
			e.printStackTrace();
		}
		
		return f=true;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
