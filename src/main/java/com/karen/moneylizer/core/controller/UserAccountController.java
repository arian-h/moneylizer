package com.karen.moneylizer.core.controller;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.karen.moneylizer.core.entity.userAccount.UserAccountEntity;
import com.karen.moneylizer.core.entity.userAccountActivationCode.UserAccountActivationCodeDTO;
import com.karen.moneylizer.core.service.AccountActiveException;
import com.karen.moneylizer.core.service.AccountNotResetException;
import com.karen.moneylizer.core.service.InvalidActivationCodeException;
import com.karen.moneylizer.core.service.InactiveAccountException;
import com.karen.moneylizer.core.service.InvalidCredentialsException;
import com.karen.moneylizer.core.service.InvalidResetTokenException;

public interface UserAccountController {

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public UserAccountEntity login(
			@Valid @RequestBody UserAccountEntity account,
			HttpServletResponse response) throws InvalidCredentialsException, InactiveAccountException;

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public UserAccountEntity create(
			@Valid @RequestBody UserAccountEntity userAccount,
			HttpServletResponse response) throws EntityExistsException, InvalidCredentialsException, InactiveAccountException;

	@RequestMapping(value = "/activate", method = RequestMethod.POST)
	public UserAccountEntity activate(
			@Valid @RequestBody UserAccountActivationCodeDTO activationCode,
			HttpServletResponse response) throws AccountActiveException,
			InvalidActivationCodeException, InvalidCredentialsException,
			InactiveAccountException;

	/*
	 * triggers reset process for existing and active accounts
	 * for inactive accounts throw InactiveAccountException
	 */
	@RequestMapping(value = "/reset", method = RequestMethod.POST)
	public void reset(@RequestParam(value = "username") String username,
			HttpServletResponse response) throws InvalidCredentialsException,
			InactiveAccountException;

	@RequestMapping(value = "/reset", method = RequestMethod.POST)
	public void reset(@Valid @RequestBody UserAccountEntity userAccount,
			@RequestParam(value = "reset") String resetToken,
			HttpServletResponse response) throws InvalidCredentialsException,
			InvalidResetTokenException, AccountNotResetException;
	
}