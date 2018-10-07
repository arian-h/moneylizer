package com.karen.moneylizer.core.service;

public class InactiveAccountException extends Exception {

	private static final long serialVersionUID = 5706339083554176543L;
	private static final String errorMessage = "User %s is inactive";

	public InactiveAccountException(String username) {
		super(String.format(errorMessage, username));
	}

}
