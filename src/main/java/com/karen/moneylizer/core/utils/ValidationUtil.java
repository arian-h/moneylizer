package com.karen.moneylizer.core.utils;

import java.util.Optional;
import java.util.regex.Pattern;

import org.apache.commons.validator.routines.EmailValidator;

public class ValidationUtil {

    public static Optional<String> isUsernameValid(String username) {
		if (username == null || !EmailValidator.getInstance().isValid(username)) {
			return Optional.of("Username must be a valid email address");
		}
		return Optional.empty();
    }

    public static Optional<String> validatePassword(String password) {
    	if (password == null || password.length() > 30 || password.length() < 8) {
    		return Optional.of("Password must be at least 8 and at most 30 characters");
    	}
    	if (!Pattern.compile( "[0-9]" ).matcher(password).find()) { // it doesn't contain any digit
    		return Optional.of("Password must have at least one digit");
        }
    	if (password.toUpperCase().equals(password)) { //it's all upper-case
    		return Optional.of("Password must have at least one lower case character");
    	}
    	if (password.toLowerCase().equals(password)) { //it's all lower-case
    		return Optional.of("Password must have at least one upper case character");
    	}
    	return Optional.empty();
    }

    public static Optional<String> validateName(String name) {
    	if (name == null || name.length() < 2) {
    		return Optional.of("Name must have at least 5 characters");
    	}
    	return Optional.empty();
    }

}