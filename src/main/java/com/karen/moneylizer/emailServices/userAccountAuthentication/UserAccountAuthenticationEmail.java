package com.karen.moneylizer.emailServices.userAccountAuthentication;

import com.karen.moneylizer.core.utils.FileUtil;
import com.karen.moneylizer.emailService.Email;

public abstract class UserAccountAuthenticationEmail extends Email {

	private final static String DEFAULT_SENDER_EMAIL_ADDRESS = FileUtil
			.readPropertyFile("application.properties",
					"email.authentication.senderEmailAddress");

	public String getSender() {
		return DEFAULT_SENDER_EMAIL_ADDRESS;
	}

}
