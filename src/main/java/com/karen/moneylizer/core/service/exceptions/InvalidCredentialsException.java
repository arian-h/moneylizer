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
	private static final String INVALID_CREDENTIALS_MSG = "Bad credentials";

	public InvalidCredentialsException(Throwable exc) {
		super(INVALID_CREDENTIALS_MSG, exc);
	}

	public InvalidCredentialsException() {
		super(INVALID_CREDENTIALS_MSG);
	}

}
