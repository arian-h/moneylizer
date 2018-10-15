package com.karen.moneylizer.emailServices;


public abstract class Email {

	private String username, subject, htmlBody, textBody, sender;
	private String[] recipients;

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getHtmlBody() {
		return htmlBody;
	}
	public void setHtmlBody(String htmlBody) {
		this.htmlBody = htmlBody;
	}
	public String getTextBody() {
		return textBody;
	}
	public void setTextBody(String textBody) {
		this.textBody = textBody;
	}
	public String[] getRecipients() {
		return recipients;
	}
	public void setRecipients(String[] recipients) {
		this.recipients = recipients;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}

}
