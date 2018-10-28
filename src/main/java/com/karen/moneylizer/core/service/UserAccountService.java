package com.karen.moneylizer.core.service;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.karen.moneylizer.core.entity.userAccount.UserAccountEntity;
import com.karen.moneylizer.core.service.exceptions.UnconfirmedUsernameException;
import com.karen.moneylizer.core.service.exceptions.UsernameConfirmationException;
import com.karen.moneylizer.core.service.exceptions.AccountResetException;
import com.karen.moneylizer.core.service.exceptions.InvalidCredentialsException;

public interface UserAccountService extends UserDetailsService {

	/*
	 * it should have been called "loadUserAccountByUsername"
	 */
	public UserAccountEntity loadUserByUsername(String username);

	/*
	 * Save if username is not taken
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
	 * Confirm the username/email address
	 */
	public void doConfirmUsername(String username, String confirmationCode)
			throws InvalidCredentialsException, UsernameConfirmationException;

	/*
	 * Request to resend username confirmation email
	 */
	void triggerConfirmUsername(String username) throws UsernameConfirmationException;

	/*
	 * Trigger password reset for a user account
	 * Sends an email to the user with reset token in it
	 */
	public void triggerReset(String username) throws UnconfirmedUsernameException;

	/*
	 * Resets user password
	 */
	void doReset(UserAccountEntity userAccountParam, String resetCodeParam)
			throws AccountResetException, InvalidCredentialsException;


}
