package com.karen.moneylizer.core.service.exceptions;

public class UsernameConfirmationException extends RuntimeException {

	private static final long serialVersionUID = -5883351454663191036L;
	private static final String exceptionMessage = "Something went wrong while confirming the email address";

	public UsernameConfirmationException(String username) {
		super(String.format(exceptionMessage, username));
	}

}
