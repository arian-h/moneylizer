package com.karen.moneylizer.core.entity.userAccountActivationCode;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserAccountActivationCodeDTO {

	private String username, password, activationCode;

	@JsonCreator
	public UserAccountActivationCodeDTO(
			@JsonProperty(value = "username", required = true) final String username,
			@JsonProperty(value = "password", required = true) final String password,
			@JsonProperty(value = "activationCode", required = true) final String activationCode) {
		this.username = username;
		this.password = password;
		this.activationCode = activationCode;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getActivationCode() {
		return activationCode;
	}

}
