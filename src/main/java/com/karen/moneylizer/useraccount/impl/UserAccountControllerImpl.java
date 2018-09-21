package com.karen.moneylizer.useraccount.impl;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;

import com.karen.moneylizer.useraccount.UserAccountController;
import com.karen.moneylizer.useraccount.UserAccountEntity;
import com.karen.moneylizer.useraccount.UserAccountService;

public class UserAccountControllerImpl implements UserAccountController {

	@Autowired
	private UserAccountService userAccountService;

	@Override
	public String login(@RequestBody UserAccountEntity account,
			HttpServletResponse response) throws IOException {
		return userAccountService.authenticateUserAndSetResponsenHeader(
				account.getUsername(), account.getPassword(), response);
	}

	@Override
	public String create(@RequestBody UserAccountEntity userAccount,
			HttpServletResponse response, BindingResult result) {
		String username = userAccount.getUsername();
		String password = userAccount.getPassword();
		// createUserDetailsDtoValidator.validate(userDetailsDTO, result);
		// if (result.hasErrors()) {
		// throw new
		// InputValidationException(result.getFieldError().getField());
		// }
		userAccountService.saveIfNotExists(username, password);
		return userAccountService.authenticateUserAndSetResponsenHeader(
				username, password, response);
	}
}
