package com.karen.moneylizer.core.service;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.karen.moneylizer.core.entity.userAccount.UserAccountEntity;
import com.karen.moneylizer.core.service.exceptions.InactiveAccountException;
import com.karen.moneylizer.core.service.exceptions.InvalidAccountActivationException;
import com.karen.moneylizer.core.service.exceptions.InvalidAccountResetActionException;
import com.karen.moneylizer.core.service.exceptions.InvalidCredentialsException;

public interface UserAccountService extends UserDetailsService {

	/*
	 * it should have been called "loadUserAccountByUsername"
	 */
	public UserAccountEntity loadUserByUsername(String username);

	/*
	 * Save if username is not taken or it's expired before activation
	 */
	public void saveIfNotExistsOrExpired(String username, String password)
			throws EntityExistsException;

	/*
	 * Validates credentials and set the response header with the JWT token
	 * 
	 * @return id of the authenticated user
	 */
	public UserAccountEntity authenticateUserAndSetResponsenHeader(
			String username, String password, HttpServletResponse response)
			throws InvalidCredentialsException;

	/*
	 * Validates the activationCode and activates the account
	 */
	public void activateAccount(String username, String activationCode)
			throws InvalidAccountActivationException;

	/*
	 * Trigger password reset for a user account
	 * Sends an email to the user with reset token in it
	 */
	public void doReset(String username) throws InactiveAccountException;

	/*
	 * Resets user password
	 */
	void reset(UserAccountEntity userAccountParam, String resetCodeParam)
			throws InvalidAccountResetActionException, InvalidCredentialsException;

}
