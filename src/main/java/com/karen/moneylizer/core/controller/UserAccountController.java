package com.karen.moneylizer.core.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.karen.moneylizer.core.entity.useraccount.UserAccountEntity;

public interface UserAccountController {

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public UserAccountEntity login(@Valid @RequestBody UserAccountEntity account,
			HttpServletResponse response) throws IOException;

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public UserAccountEntity create(@Valid @RequestBody UserAccountEntity userAccount,
			HttpServletResponse response);

}