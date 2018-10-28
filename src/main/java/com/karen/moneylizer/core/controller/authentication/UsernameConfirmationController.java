package com.karen.moneylizer.core.controller.authentication;

import java.security.Principal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.karen.moneylizer.core.service.exceptions.UsernameConfirmationException;
import com.karen.moneylizer.core.service.exceptions.InvalidCredentialsException;

public interface UsernameConfirmationController {

	@RequestMapping(value = "/confirm", method = RequestMethod.GET)
	public void doConfirm(
			@RequestParam(value = "confirmationCode") String confirmationCode, Principal principal)
			throws UsernameConfirmationException,
			InvalidCredentialsException;

	@RequestMapping(value = "/renewConfirmation", method = RequestMethod.GET)
	public void triggerConfirmation(Principal principal) throws UsernameConfirmationException;

}