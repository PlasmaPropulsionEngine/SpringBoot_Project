package com.contactmanager.helper;

//this class is used to show messages using bootstrap css-classes and thymeleaf to show in front-end
public class Messages {


	private String content;
	
	private String type;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Messages(String content, String type) {
		super();
		this.content = content;
		this.type = type;
	}
	
	
	
}
