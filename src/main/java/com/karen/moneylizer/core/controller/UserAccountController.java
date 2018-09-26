package com.karen.moneylizer.core.controller;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.karen.moneylizer.core.entity.useraccount.UserAccountEntity;

public interface UserAccountController {

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public UserAccountEntity login(@Valid @RequestBody UserAccountEntity account,
			HttpServletResponse response) throws BadCredentialsException;

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public UserAccountEntity create(@Valid @RequestBody UserAccountEntity userAccount,
			HttpServletResponse response) throws EntityExistsException;

}