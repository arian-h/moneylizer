package com.karen.moneylizer.emailServices.userAccountAuthenticationEmail;

public class UserAccountActivationEmail extends UserAccountAuthenticationEmail {

	private static final long serialVersionUID = 7463488596533154777L;

	private static final String HTMLBODY = "<h1>Amazon SES test (AWS SDK for Java)</h1>"
		      + "<p>This email was sent with <a href='https://aws.amazon.com/ses/'>"
		      + "Amazon SES</a> using the <a href='https://aws.amazon.com/sdk-for-java/'>" 
		      + "AWS SDK for Java</a>";

	private static final String TEXTBODY = "This email was sent through Amazon SES "
		      + "using the AWS SDK for Java."; //TODO fix these values

	public UserAccountActivationEmail() {}

	@Override
	public String getHtmlBody() {
		return HTMLBODY;
	}

	@Override
	public String getTextBody() {
		return TEXTBODY;
	}

}
