package com.karen.moneylizer.core.controller.impl;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.karen.moneylizer.core.controller.UserAccountController;
import com.karen.moneylizer.core.entity.useraccount.UserAccountEntity;
import com.karen.moneylizer.core.entity.useraccount.UserAccountValidator;
import com.karen.moneylizer.core.service.UserAccountService;

@RestController
@RequestMapping("/api/authentication")
public class UserAccountControllerImpl implements UserAccountController {

	@Autowired
	private UserAccountService userAccountService;

	@Override
	public UserAccountEntity login(@Valid @RequestBody UserAccountEntity account,
			HttpServletResponse response) throws BadCredentialsException {
		return userAccountService.authenticateUserAndSetResponsenHeader(
				account.getUsername(), account.getPassword(), response);
	}

	@Override
	public UserAccountEntity create(@Valid @RequestBody UserAccountEntity userAccount,
			HttpServletResponse response) throws EntityExistsException {
		String username = userAccount.getUsername();
		String password = userAccount.getPassword();
		userAccountService.saveIfNotExistsOrExpired(username, password);
		return userAccountService.authenticateUserAndSetResponsenHeader(
				username, password, response);
	}

	//used to bind the validator to the incoming request
	@InitBinder
	public void binder(WebDataBinder binder) {
		binder.addValidators(new UserAccountValidator());
	}
}
