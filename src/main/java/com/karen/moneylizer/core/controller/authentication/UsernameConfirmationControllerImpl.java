package com.karen.moneylizer.core.controller.authentication;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.karen.moneylizer.core.service.UserAccountService;
import com.karen.moneylizer.core.service.exceptions.UsernameConfirmationException;
import com.karen.moneylizer.core.service.exceptions.InvalidCredentialsException;

@RestController
@RequestMapping("/api/confirmation")
public class UsernameConfirmationControllerImpl implements UsernameConfirmationController {

	@Autowired
	private UserAccountService userAccountService;

	@Override
	public void doConfirm(String confirmationCode, Principal principal)
			throws UsernameConfirmationException,
			InvalidCredentialsException {
		userAccountService.doConfirmUsername(principal.getName(), confirmationCode);
	}

	@Override
	public void triggerConfirmation(Principal principal) throws UsernameConfirmationException {
		userAccountService.triggerConfirmUsername(principal.getName());
	}

	
}