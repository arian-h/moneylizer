package com.karen.moneylizer.core.service.exceptions;

public class AccountResetException extends RuntimeException {

	private static final long serialVersionUID = -5883351454663191036L;
	private static final String exceptionMessage = "Password reset was not successful";

	public AccountResetException() {
		super(exceptionMessage);
	}

}
