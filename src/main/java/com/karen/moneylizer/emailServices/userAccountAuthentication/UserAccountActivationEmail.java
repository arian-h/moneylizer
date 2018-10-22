package com.karen.moneylizer.emailServices.userAccountAuthentication;

import com.karen.moneylizer.core.utils.FileUtil;

public class UserAccountActivationEmail extends UserAccountAuthenticationEmail {

	private static final String HTMLBODY = FileUtil.readFile(AUTHENTICATION_EMAIL_DIR + "activation_email.html");
	private static final String TEXTBODY = FileUtil.readFile(AUTHENTICATION_EMAIL_DIR + "activation_email.txt");
	private static final String SUBJECT = "Welcome to Moneylizer! Activate your account";

	private String activationCode;

	@Override
	public String getSubject() {
		return SUBJECT;
	}

	@Override
	public String getTextBody() {
		return String.format(TEXTBODY, this.activationCode);
	}
	
	@Override
	public String getHtmlBody() {
		return String.format(HTMLBODY, this.activationCode);
	}

	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}

	public String getActivationCode() {
		return this.activationCode;
	}

}