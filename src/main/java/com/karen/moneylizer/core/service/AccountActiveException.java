package com.karen.moneylizer.core.service;

public class AccountActiveException extends Exception {

	private static final long serialVersionUID = -5883351454663191036L;

	public AccountActiveException(String msg) {
		super(msg);
	}

}
