package com.karen.moneylizer.core.service.exceptions;

/**
 * This is different from BadCredentialsException
 * BadCredentialException class extends RuntimeException and is thrown by
 * the authentication manager
 * @author root
 *
 */
public class InvalidCredentialsException extends Exception {

	private static final long serialVersionUID = 3626997596681382830L;
	private static final String INCORRECT_ACTIVATION_CODE_MSG = "Bad credentials";

	public InvalidCredentialsException(Throwable exc) {
		super(INCORRECT_ACTIVATION_CODE_MSG, exc);
	}

	public InvalidCredentialsException() {
		super(INCORRECT_ACTIVATION_CODE_MSG);
	}

}
