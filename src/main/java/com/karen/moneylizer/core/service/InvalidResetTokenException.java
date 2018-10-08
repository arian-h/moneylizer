package com.karen.moneylizer.core.service;

public class InvalidResetTokenException extends Exception {

	private static final long serialVersionUID = -5883351454663191036L;
	private static final String exceptionMessage = "Invalid activation code";

	public InvalidResetTokenException() {
		super(exceptionMessage);
	}

}
