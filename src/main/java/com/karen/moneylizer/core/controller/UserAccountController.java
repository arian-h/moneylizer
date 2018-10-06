package com.karen.moneylizer.core.controller;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.karen.moneylizer.core.entity.userAccount.UserAccountEntity;
import com.karen.moneylizer.core.entity.userAccountActivationCode.UserAccountActivationCodeDTO;
import com.karen.moneylizer.core.service.AccountActiveException;
import com.karen.moneylizer.core.service.BadActivationCodeException;
import com.karen.moneylizer.core.service.InactiveAccountException;
import com.karen.moneylizer.core.service.InvalidCredentialsException;

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
			BadActivationCodeException, InvalidCredentialsException,
			InactiveAccountException;

}