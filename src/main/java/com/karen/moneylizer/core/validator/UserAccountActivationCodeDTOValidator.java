package com.karen.moneylizer.core.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.karen.moneylizer.core.entity.userAccountActivationCode.UserAccountActivationCodeDTO;
import com.karen.moneylizer.core.utils.ValidationUtil;


public class UserAccountActivationCodeDTOValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return UserAccountActivationCodeDTO.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		UserAccountActivationCodeDTO activationCode = (UserAccountActivationCodeDTO) target;
		if (!ValidationUtil.isUsernameValid(activationCode.getUsername())) {
			errors.reject("Username must be a valid email address");			
		}
		String passwordError = ValidationUtil.validatePassword(activationCode.getPassword());
		if (passwordError != null) {
			errors.reject(passwordError);
		}
		if (!ValidationUtil.isActivationCodeValid(activationCode.getActivationCode())) {
			errors.reject("Activation code entails 5 alphanumeric characters");
		}
	}

}
