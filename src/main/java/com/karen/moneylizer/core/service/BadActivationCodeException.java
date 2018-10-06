package com.karen.moneylizer.core.service;

public class BadActivationCodeException extends Exception {

	private static final long serialVersionUID = 3626997596681382830L;
	private static final String INCORRECT_ACTIVATION_CODE_MSG = "Incorrect activation code";

	public BadActivationCodeException() {
		super(INCORRECT_ACTIVATION_CODE_MSG);
	}
}
