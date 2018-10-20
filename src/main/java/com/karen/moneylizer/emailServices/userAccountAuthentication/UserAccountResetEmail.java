package com.karen.moneylizer.emailServices.userAccountAuthentication;

import com.karen.moneylizer.core.utils.FileUtil;

public class UserAccountResetEmail extends UserAccountAuthenticationEmail {

	private String resetCode;

	private static final String HTMLBODY = FileUtil.readFile("public/static/email/authentication/reset_email.html");
	private static final String TEXTBODY = FileUtil.readFile("public/static/email/authentication/reset_email.html");

	private static final String SUBJECT = "Welcome to Moneylizer! Activate your account";

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

	public void setResetCode(String resetCode) {
		this.resetCode = resetCode;
	}

	public String getResetCode() {
		return this.resetCode;
	}
}
