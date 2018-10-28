package com.karen.moneylizer.core.controller.authentication.dto;

import org.springframework.stereotype.Service;

import com.karen.moneylizer.core.utils.ValidationUtil;

@Service
public class UserAccountRegisterationDtoValidator {

	public void validate(UserAccountRegisterationDto target) {
		ValidationUtil.isUsernameValid(target.getUsername()).ifPresent(
				error -> {
					throw new IllegalArgumentException(error);
				});
		ValidationUtil.validatePassword(target.getPassword()).ifPresent(
				error -> {
					throw new IllegalArgumentException(error);
				});
		ValidationUtil.validateName(target.getName()).ifPresent(error -> {
			throw new IllegalArgumentException(error);
		});
	}

}