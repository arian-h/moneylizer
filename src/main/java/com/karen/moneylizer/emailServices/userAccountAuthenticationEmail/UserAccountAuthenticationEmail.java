package com.karen.moneylizer.emailServices.userAccountAuthenticationEmail;

import java.io.Serializable;

import com.karen.moneylizer.emailServices.Email;

public abstract class UserAccountAuthenticationEmail extends Email implements Serializable {

	private static final long serialVersionUID = -4859763825523735932L;

	private final static String SENDER_EMAIL_ADDRESS = "aryan.hosseinzadeh@gmail.com"; // change it to application.properties

	@Override
	public String getSender() {
		return SENDER_EMAIL_ADDRESS;
	}

}