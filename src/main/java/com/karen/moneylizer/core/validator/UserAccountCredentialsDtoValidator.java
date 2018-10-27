package com.karen.moneylizer.core.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.karen.moneylizer.core.controller.authentication.UserAccountCredentialsDto;
import com.karen.moneylizer.core.utils.ValidationUtil;

public class UserAccountCredentialsDtoValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return UserAccountCredentialsDto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		UserAccountCredentialsDto userAccount = (UserAccountCredentialsDto) target;
		ValidationUtil.isUsernameValid(userAccount.getUsername()).ifPresent(error -> errors.reject(error));
		ValidationUtil.validatePassword(userAccount.getPassword()).ifPresent(error -> errors.reject(error));
	}

}