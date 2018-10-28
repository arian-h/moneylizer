package com.karen.moneylizer.core.controller.authentication.dto;

import org.springframework.stereotype.Service;

import com.karen.moneylizer.core.utils.ValidationUtil;

@Service
public class UserAccountResetDtoValidator {

	public void validate(UserAccountResetDto target) {
		ValidationUtil.isUsernameValid(target.getUsername()).ifPresent(
				error -> {
					throw new IllegalArgumentException(error);
				});
		ValidationUtil.validatePassword(target.getPassword()).ifPresent(
				error -> {
					throw new IllegalArgumentException(error);
				});
	}

}