package com.karen.moneylizer.core.service.exceptions;

public class UnconfirmedUsernameException extends RuntimeException {

	private static final long serialVersionUID = 5706339083554176543L;
	private static final String errorMessage = "Email address is not confirmed";

	public UnconfirmedUsernameException(String username) {
		super(String.format(errorMessage, username));
	}

}