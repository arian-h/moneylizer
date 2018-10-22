package com.karen.moneylizer.emailServices.userAccountAuthentication;

import com.karen.moneylizer.core.utils.FileUtil;
import com.karen.moneylizer.emailService.Email;

public abstract class UserAccountAuthenticationEmail extends Email {

	private final static String DEFAULT_SENDER_EMAIL_ADDRESS = FileUtil
			.readPropertyFile("application.properties",
					"email.authentication.senderEmailAddress");
	protected static final String AUTHENTICATION_EMAIL_DIR = "public/static/email/authentication/";

	public String getSender() {
		return DEFAULT_SENDER_EMAIL_ADDRESS;
	}

}