package com.karen.moneylizer.core.controller.authentication;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsernameConfirmationDto implements Serializable {

	private static final long serialVersionUID = -6100449766156664907L;

	private String username;

	private String confirmationCode;

}
