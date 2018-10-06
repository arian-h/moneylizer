package com.karen.moneylizer.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

public class ValidationUtil {

    public static List<String> fromBindingErrors(Errors errors) {
        List<String> validErrors = new ArrayList<String>();
        for (ObjectError objectError : errors.getAllErrors()) {
            validErrors.add(objectError.getCode());
        }
        return validErrors;
    }

    public static boolean isUsernameValid(String username) {
		return EmailValidator.getInstance().isValid(username);
    }

    public static String validatePassword(String password) {
    	if (password.length() > 30 || password.length() < 8) {
    		return "Password must be at least 8 and at most 30 characters";
    	}
    	if (!Pattern.compile( "[0-9]" ).matcher(password).find()) { // it doesn't contain any digit
    		return "Password must have at least one digit";
        }
    	if (password.toUpperCase().equals(password)) { //it's all upper-case
    		return "Password must have at least one lower case character";
    	}
    	if (password.toLowerCase().equals(password)) { //it's all lower-case
    		return "Password must have at least one upper case character";
    	}
    	return null;
    }

    public static boolean isActivationCodeValid(String activationCode) {
    	return Pattern.compile("[a-zA-Z0-9]{5}").matcher(activationCode).find();
    }
}