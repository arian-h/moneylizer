package com.karen.moneylizer.core.entity.useraccount;

import java.util.regex.Pattern;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class UserAccountValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return UserAccountEntity.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		UserAccountEntity userAccount = (UserAccountEntity) target;
		String password = userAccount.getPassword();
    	if (password.length() > 30 || password.length() < 8) {
    		errors.reject("Password must be at least 8 and at most 30 characters");
    	}
    	if (!Pattern.compile( "[0-9]" ).matcher(password).find()) { // it doesn't contain any digit
    		errors.reject("Password must have at least one digit");
        }
    	if (password.toUpperCase().equals(password)) { //it's all upper-case
    		errors.reject("Password must have at least one lower case character");
    	}
    	if (password.toLowerCase().equals(password)) { //it's all lower-case
    		errors.reject("Password must have at least one upper case character");
    	}
	}

}