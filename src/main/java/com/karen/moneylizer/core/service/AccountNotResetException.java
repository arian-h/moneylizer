package com.karen.moneylizer.core.service;

/**
 * Account is expected to be reset but is not
 * @author root
 *
 */
public class AccountNotResetException extends Exception {

	private static final long serialVersionUID = 6641498033672826293L;
	private static final String exceptionMessage = "Account has not been reset";

	public AccountNotResetException() {
		super(exceptionMessage);
	}

}
