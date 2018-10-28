package com.karen.moneylizer.emailServices.userAccountAuthentication;

import lombok.Getter;
import lombok.Setter;

import com.karen.moneylizer.core.utils.FileUtil;

public class UsernameConfirmationEmail extends UserAccountAuthenticationEmail {

	private static final String HTMLBODY = FileUtil.readFile(AUTHENTICATION_EMAIL_DIR + "username_confirmation_email.html");
	private static final String TEXTBODY = FileUtil.readFile(AUTHENTICATION_EMAIL_DIR + "username_confirmation_email.txt");
	private static final String SUBJECT = "Welcome to Moneylizer! Activate your account";

	@Getter
	@Setter
	private String confirmationCode;

	@Override
	public String getSubject() {
		return SUBJECT;
	}

	@Override
	public String getTextBody() {
		return String.format(TEXTBODY, this.confirmationCode);
	}

	@Override
	public String getHtmlBody() {
		return String.format(HTMLBODY, this.confirmationCode);
	}

}