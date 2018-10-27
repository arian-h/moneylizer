package com.karen.moneylizer.core.controller.authentication;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.karen.moneylizer.core.entity.userAccount.UserAccountEntity;
import com.karen.moneylizer.core.service.exceptions.InactiveAccountException;
import com.karen.moneylizer.core.service.exceptions.InvalidAccountActivationException;
import com.karen.moneylizer.core.service.exceptions.InvalidAccountResetActionException;
import com.karen.moneylizer.core.service.exceptions.InvalidCredentialsException;

public interface AuthenticationController {

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public UserAccountEntity login(
			@Valid @RequestBody UserAccountCredentialsDto userAccount,
			HttpServletResponse response) throws InvalidCredentialsException, InactiveAccountException;

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public UserAccountEntity create(
			@Valid @RequestBody UserAccountCredentialsDto userAccount,
			HttpServletResponse response) throws EntityExistsException, InvalidCredentialsException, InactiveAccountException;

	@RequestMapping(value = "/activate", method = RequestMethod.POST)
	public void activate(
			@Valid @RequestBody UserAccountActivationDto accountActivationDto,
			HttpServletResponse response) throws InvalidAccountActivationException, InvalidCredentialsException;

	/*
	 * triggers reset process for existing and active accounts
	 * for inactive accounts throw InactiveAccountException
	 */
	@RequestMapping(value = "/reset", method = RequestMethod.GET)
	public void doReset(@RequestParam(value = "username") String username,
			HttpServletResponse response) throws InactiveAccountException;

	@RequestMapping(value = "/reset", method = RequestMethod.POST)
	public void reset(@Valid @RequestBody UserAccountEntity userAccount,
			@RequestParam(value = "token") String token,
			HttpServletResponse response) throws InvalidCredentialsException,
			InvalidAccountResetActionException;

}