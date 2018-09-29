package com.karen.moneylizer.core.service;

public class InactiveAccountException extends RuntimeException {

	private static final long serialVersionUID = 5706339083554176543L;

	private String errorMessage;

	public InactiveAccountException(String msg) {
		this.errorMessage = msg;
	}

	public String getMessage() {
		return this.errorMessage;
	}
}
