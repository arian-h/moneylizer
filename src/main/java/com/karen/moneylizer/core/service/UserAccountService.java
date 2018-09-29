package com.karen.moneylizer.core.service;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.karen.moneylizer.core.entity.useraccount.UserAccountEntity;

public interface UserAccountService extends UserDetailsService {

	/*
	 * it should have been called "loadUserAccountByUsername"
	 */
	public UserAccountEntity loadUserByUsername(String username)
			throws InactiveAccountException, UsernameNotFoundException;

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
			throws BadCredentialsException;

}
