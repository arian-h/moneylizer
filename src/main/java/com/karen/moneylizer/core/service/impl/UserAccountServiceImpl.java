package com.karen.moneylizer.core.service.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.karen.moneylizer.core.entity.user.UserEntity;
import com.karen.moneylizer.core.entity.useraccount.UserAccountEntity;
import com.karen.moneylizer.core.repository.UserAccountRepository;
import com.karen.moneylizer.core.service.UserAccountService;
import com.karen.moneylizer.security.SecurityConstants;

@Service
@Transactional
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
		if (account == null) {
			throw new UsernameNotFoundException(String.format(
					"User %s was not found", username));
		}
		return account;
	}

	@Override
	public void saveIfNotExists(String username, String password)
			throws EntityExistsException {
		if (userAccountRepository.existsByUsername(username)) {
			throw new EntityExistsException(String.format(
					"Username %s is not available", username));
		}
		UserEntity user = new UserEntity();
		UserAccountEntity userAccount = new UserAccountEntity(username,
				passwordEncoder.encode(password));
		userAccount.setUser(user);
		userAccountRepository.save(userAccount);
	}

	@Override
	public UserAccountEntity authenticateUserAndSetResponsenHeader(String username,
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
		return (UserAccountEntity) authentication.getPrincipal();
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
