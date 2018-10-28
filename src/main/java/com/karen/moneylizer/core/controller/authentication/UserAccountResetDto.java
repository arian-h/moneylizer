package com.karen.moneylizer.core.controller.authentication;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAccountResetDto implements Serializable {

	private static final long serialVersionUID = 8787885124961537778L;

	private String Username;
	
	private String password;

}