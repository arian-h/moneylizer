package com.karen.moneylizer.emailServices.userAccountAuthentication;

import lombok.Getter;
import lombok.Setter;

import com.karen.moneylizer.core.utils.FileUtil;

public class UserAccountResetEmail extends UserAccountAuthenticationEmail {

	private static final String HTMLBODY = FileUtil.readFile(AUTHENTICATION_EMAIL_DIR + "reset_email.html");
	private static final String TEXTBODY = FileUtil.readFile(AUTHENTICATION_EMAIL_DIR + "reset_email.txt");
	private static final String SUBJECT = "Welcome to Moneylizer! Activate your account";

	@Getter
	@Setter
	private String resetCode;

	@Override
	public String getSubject() {
		return SUBJECT;
	}

	@Override
	public String getTextBody() {
		return String.format(TEXTBODY, this.resetCode);
	}
	
	@Override
	public String getHtmlBody() {
		return String.format(HTMLBODY, this.resetCode);
	}

}
