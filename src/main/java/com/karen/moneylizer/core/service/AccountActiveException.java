package com.karen.moneylizer.core.service;

public class AccountActiveException extends Exception {

	private static final long serialVersionUID = -5883351454663191036L;
	private static final String exceptionMessage = "User %s is already active";

	public AccountActiveException(String username) {
		super(String.format(exceptionMessage, username));
	}

}
