package com.karen.moneylizer.useraccount.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.karen.moneylizer.security.SecurityConstants;
import com.karen.moneylizer.user.UserEntity;
import com.karen.moneylizer.useraccount.UserAccountEntity;
import com.karen.moneylizer.useraccount.UserAccountRepository;
import com.karen.moneylizer.useraccount.UserAccountService;

public class UserAccountServiceImpl implements UserAccountService {

	@Autowired
	private UserAccountRepository userAccountRepository;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserAccountEntity loadUserByUsername(String username)
			throws UsernameNotFoundException {
		UserAccountEntity account = userAccountRepository
				.findByUsername(username);
		if (account != null) {
			return account;
		}
		throw new UsernameNotFoundException(String.format(
				"User %s was not found", username));
	}

	@Override
	public void saveIfNotExists(String username, String password) {
		if (!userAccountRepository.existsByUsername(username)) {
			UserEntity user = new UserEntity();
			UserAccountEntity userAccount = new UserAccountEntity(username,
					passwordEncoder.encode(password));
			userAccount.setUser(user);
			userAccountRepository.save(userAccount);
		} else {
			throw new EntityExistsException(String.format(
					"Username %s is not available", username));			
		}
	}

	@Override
	public String authenticateUserAndSetResponsenHeader(String username,
			String password, HttpServletResponse response)
			throws BadCredentialsException {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(username,
						password));
		if (authentication == null) {
			throw new BadCredentialsException("Bad username/password presented");
		}
		response.addHeader(SecurityConstants.AUTHENTICATION_HEADER, String
				.format("%s %s", SecurityConstants.BEARER,
						generateJwtToken(username)));
		SecurityContextHolder.getContext()
				.setAuthentication(authentication);
		return ((UserAccountEntity) (authentication.getPrincipal()))
				.getId();
	}

	private String generateJwtToken(String username) {
		return Jwts
				.builder()
				.setSubject(username)
				.setExpiration(
						new Date(System.currentTimeMillis()
								+ SecurityConstants.EXPIRATIONTIME))
				.signWith(SignatureAlgorithm.HS512,
						SecurityConstants.JWT_SECRET).compact();
	}
}
