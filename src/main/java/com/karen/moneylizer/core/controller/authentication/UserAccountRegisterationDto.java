package com.karen.moneylizer.core.controller.authentication;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAccountRegisterationDto implements Serializable {

	private static final long serialVersionUID = -6562093952548208523L;

	private String username;

	private String password;

	private String name;
}
