package com.karen.moneylizer.core.service.exceptions;

public class InvalidAccountActivationException extends Exception {

	private static final long serialVersionUID = -5883351454663191036L;
	private static final String exceptionMessage = "User %s is already active";

	public InvalidAccountActivationException(String username) {
		super(String.format(exceptionMessage, username));
	}

}
