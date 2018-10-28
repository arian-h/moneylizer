package com.karen.moneylizer.core.controller.authentication;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAccountLoginDto implements Serializable {

	private static final long serialVersionUID = 420800308374364582L;

	private String username;

	private String password;

}