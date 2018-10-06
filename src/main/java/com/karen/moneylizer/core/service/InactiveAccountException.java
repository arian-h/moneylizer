package com.karen.moneylizer.core.service;

public class InactiveAccountException extends Exception {

	private static final long serialVersionUID = 5706339083554176543L;

	public InactiveAccountException(String msg) {
		super(msg);
	}

}
