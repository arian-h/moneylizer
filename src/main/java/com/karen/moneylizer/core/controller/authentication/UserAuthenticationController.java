package com.karen.moneylizer.core.controller.authentication;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.karen.moneylizer.core.entity.userAccount.UserAccountEntity;
import com.karen.moneylizer.core.service.exceptions.AccountResetException;
import com.karen.moneylizer.core.service.exceptions.InvalidCredentialsException;
import com.karen.moneylizer.core.service.exceptions.UnconfirmedUsernameException;

public interface UserAuthenticationController {

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public UserAccountEntity login(
			@RequestBody UserAccountLoginDto userAccount,
			HttpServletResponse response) throws InvalidCredentialsException;

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public UserAccountEntity create(
			@RequestBody UserAccountRegisterationDto userAccount,
			HttpServletResponse response) throws EntityExistsException,
			InvalidCredentialsException;

	/*
	 * triggers reset process for existing and active accounts for inactive
	 * accounts throw InactiveAccountException
	 */
	@RequestMapping(value = "/reset", method = RequestMethod.GET)
	public void triggerReset(@RequestParam(value = "username") String username)
			throws UnconfirmedUsernameException;

	@RequestMapping(value = "/reset", method = RequestMethod.POST)
	public void doReset(@RequestBody UserAccountResetDto userAccount,
			@RequestParam(value = "token") String token)
			throws InvalidCredentialsException,
			AccountResetException;

}