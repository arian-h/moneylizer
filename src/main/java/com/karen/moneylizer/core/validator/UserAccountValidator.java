package com.karen.moneylizer.core.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.karen.moneylizer.core.entity.userAccount.UserAccountEntity;
import com.karen.moneylizer.core.utils.ValidationUtil;

public class UserAccountValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return UserAccountEntity.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		UserAccountEntity userAccount = (UserAccountEntity) target;
		if (!ValidationUtil.isUsernameValid(userAccount.getUsername())) {
			errors.reject("Username must be a valid email address");			
		}
		String passwordError = ValidationUtil.validatePassword(userAccount.getPassword());
		if (passwordError != null) {
			errors.reject(passwordError);
		}
	}

}