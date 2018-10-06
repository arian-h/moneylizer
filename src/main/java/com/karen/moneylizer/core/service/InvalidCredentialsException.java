package com.karen.moneylizer.core.service;

/**
 * This is different from BadCredentialsException
 * BadCredentialException class extends RuntimeException and is thrown by
 * the authentication manager
 * @author root
 *
 */
public class InvalidCredentialsException extends Exception {//TODO fix this 

	private static final long serialVersionUID = 3626997596681382830L;
	private static final String INCORRECT_ACTIVATION_CODE_MSG = "Bad credentials provided";

	public InvalidCredentialsException() {
		super(INCORRECT_ACTIVATION_CODE_MSG);
	}

}
