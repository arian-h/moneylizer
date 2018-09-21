package com.karen.moneylizer.useraccount;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
public interface UserAccountService extends UserDetailsService {

	/*
	 * it should have been called "loadUserAccountByUsername"
	 */
	@Override
	public UserAccountEntity loadUserByUsername(String username)
			throws UsernameNotFoundException;

	public void saveIfNotExists(String username, String password);

	/*
	 * Validates credentials and set the response header with the JWT token
	 * 
	 * @return id of the authenticated user
	 */
	public String authenticateUserAndSetResponsenHeader(String username,
			String password, HttpServletResponse response)
			throws BadCredentialsException;

}
