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
import com.karen.moneylizer.core.entity.userAccountActivityCodeEntity.UserAccountActivityCodeEntity;
import com.karen.moneylizer.core.entity.useraccount.UserAccountEntity;
import com.karen.moneylizer.core.repository.UserAccountRepository;
import com.karen.moneylizer.core.service.InactiveAccountException;
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
			throws InactiveAccountException, UsernameNotFoundException {
		UserAccountEntity userAccount = userAccountRepository.findByUsername(username);
		if (userAccount == null) {
			throw new UsernameNotFoundException(String.format("User %s was not found", username));
		} else if (userAccount.isExpired()) {
			userAccountRepository.delete(userAccount);
			throw new UsernameNotFoundException(String.format("User %s was not found", username)); 
		} else if (!userAccount.isActive()) {
			throw new InactiveAccountException("Your account is inactive");
		}
		return userAccount;
	}

	@Override
	public void saveIfNotExistsOrExpired(String username, String password)
			throws EntityExistsException {
		UserAccountEntity userAccount = userAccountRepository.findByUsername(username);
		if (userAccount != null) {
			if (userAccount.isExpired()) {
				userAccountRepository.delete(userAccount);					
			} else {
				throw new EntityExistsException(String.format(
						"Username %s is not available", username));				
			}
		}
		userAccount = new UserAccountEntity(username,
				passwordEncoder.encode(password));
		UserEntity user = new UserEntity();
		user.setUserAccount(userAccount);
		userAccount.setUser(user);
		UserAccountActivityCodeEntity activityCode = new UserAccountActivityCodeEntity();
		activityCode.setUserAccount(userAccount);
		userAccount.setActivityCode(activityCode);
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
