package com.karen.moneylizer.core.controller.impl;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.karen.moneylizer.core.controller.UserAccountController;
import com.karen.moneylizer.core.entity.userAccount.UserAccountEntity;
import com.karen.moneylizer.core.entity.userAccountActivationCode.UserAccountActivationCodeDTO;
import com.karen.moneylizer.core.service.AccountActiveException;
import com.karen.moneylizer.core.service.BadActivationCodeException;
import com.karen.moneylizer.core.service.InactiveAccountException;
import com.karen.moneylizer.core.service.InvalidCredentialsException;
import com.karen.moneylizer.core.service.UserAccountService;
import com.karen.moneylizer.core.validator.CompoundValidator;
import com.karen.moneylizer.core.validator.UserAccountActivationCodeDTOValidator;
import com.karen.moneylizer.core.validator.UserAccountValidator;

@RestController
@RequestMapping("/api/authentication")
public class UserAccountControllerImpl implements UserAccountController {

	@Autowired
	private UserAccountService userAccountService;

 	@Override
	public UserAccountEntity login(@Valid @RequestBody UserAccountEntity account,
			HttpServletResponse response) throws InvalidCredentialsException, InactiveAccountException {
		return userAccountService.authenticateUserAndSetResponsenHeader(
				account.getUsername(), account.getPassword(), response);
	}

	@Override
	public UserAccountEntity create(@Valid @RequestBody UserAccountEntity userAccount,
			HttpServletResponse response) throws EntityExistsException, InvalidCredentialsException, InactiveAccountException {
		String username = userAccount.getUsername();
		String password = userAccount.getPassword();
		userAccountService.saveIfNotExistsOrExpired(username, password);
		return userAccountService.authenticateUserAndSetResponsenHeader(
				username, password, response);
	}

	@Override
	public UserAccountEntity activate(
			@Valid @RequestBody UserAccountActivationCodeDTO activationCode,
			HttpServletResponse response) throws AccountActiveException,
			BadActivationCodeException, InvalidCredentialsException,
			InactiveAccountException {
		String code = activationCode.getActivationCode();
		String username = activationCode.getUsername();
		String password = activationCode.getPassword();
		userAccountService.activateAccount(username, code);
		return userAccountService.authenticateUserAndSetResponsenHeader(
				username, password, response);
	}

	//used to bind the validator to the incoming request
	@InitBinder
	public void binder(WebDataBinder binder) {
		binder.addValidators(new CompoundValidator(new Validator[] {
				new UserAccountValidator(),
				new UserAccountActivationCodeDTOValidator() }));
	}
}
