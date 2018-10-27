package com.karen.moneylizer.core.service.exceptions;

public class InvalidAccountResetActionException extends Exception {

	private static final long serialVersionUID = -5883351454663191036L;
	private static final String exceptionMessage = "Invalid reset token";

	public InvalidAccountResetActionException() {
		super(exceptionMessage);
	}

}
