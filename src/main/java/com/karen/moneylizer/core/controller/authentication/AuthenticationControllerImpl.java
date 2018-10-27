package com.karen.moneylizer.core.controller.authentication;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.karen.moneylizer.core.entity.userAccount.UserAccountEntity;
import com.karen.moneylizer.core.service.UserAccountService;
import com.karen.moneylizer.core.service.exceptions.InactiveAccountException;
import com.karen.moneylizer.core.service.exceptions.InvalidAccountActivationException;
import com.karen.moneylizer.core.service.exceptions.InvalidAccountResetActionException;
import com.karen.moneylizer.core.service.exceptions.InvalidCredentialsException;
import com.karen.moneylizer.core.validator.CompoundValidator;
import com.karen.moneylizer.core.validator.UserAccountCredentialsDtoValidator;

@RestController
@RequestMapping("/api/authentication")
public class AuthenticationControllerImpl implements AuthenticationController {

	@Autowired
	private UserAccountService userAccountService;

 	@Override
	public UserAccountEntity login(@RequestBody UserAccountCredentialsDto userAccount,
			HttpServletResponse response) throws InvalidCredentialsException, InactiveAccountException {
		return userAccountService.authenticateUserAndSetResponsenHeader(
				userAccount.getUsername(), userAccount.getPassword(), response);
	}

	@Override
	public UserAccountEntity create(@Valid @RequestBody UserAccountCredentialsDto userAccount,
			HttpServletResponse response) throws EntityExistsException, InvalidCredentialsException {
		String username = userAccount.getUsername();
		String password = userAccount.getPassword();
		userAccountService.saveIfNotExistsOrExpired(username, password);
		return userAccountService.authenticateUserAndSetResponsenHeader(
				username, password, response);
	}

	@Override
	public void activate(
			@Valid @RequestBody UserAccountActivationDto accountActivation,
			HttpServletResponse response) throws InvalidAccountActivationException,
			InvalidCredentialsException {
		userAccountService.activateAccount(accountActivation.getUsername(), accountActivation.getActivationCode());
	}

	@Override
	public void doReset(@RequestParam(value = "username") String username,
			HttpServletResponse response) throws InactiveAccountException {
		userAccountService.doReset(username);
	}

	@Override
	public void reset(@Valid @RequestBody UserAccountEntity userAccount,
			@RequestParam(value = "token") String token,
			HttpServletResponse response) throws InvalidAccountResetActionException, InvalidCredentialsException {
		userAccountService.reset(userAccount, token);
	}

	//used to bind the validator to the incoming request
	@InitBinder
	public void binder(WebDataBinder binder) {
		binder.addValidators(new CompoundValidator(new Validator[] {
				new UserAccountCredentialsDtoValidator()}));
	}

}
