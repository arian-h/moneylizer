package com.karen.moneylizer.core.service.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.karen.moneylizer.core.entity.user.UserEntity;
import com.karen.moneylizer.core.entity.userAccount.UserAccountEntity;
import com.karen.moneylizer.core.entity.userAccountActivationCode.UserAccountActivationCodeEntity;
import com.karen.moneylizer.core.repository.UserAccountActivationCodeRepository;
import com.karen.moneylizer.core.repository.UserAccountRepository;
import com.karen.moneylizer.core.service.AccountActiveException;
import com.karen.moneylizer.core.service.BadActivationCodeException;
import com.karen.moneylizer.core.service.InactiveAccountException;
import com.karen.moneylizer.core.service.InvalidCredentialsException;
import com.karen.moneylizer.core.service.UserAccountService;
import com.karen.moneylizer.security.SecurityConstants;

@Service
@Transactional
public class UserAccountServiceImpl implements UserAccountService {

	@Autowired
	private UserAccountRepository userAccountRepository;

	@Autowired
	private UserAccountActivationCodeRepository userAccountActivationCodeRepository;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserAccountEntity loadUserByUsername(String username) {
		UserAccountEntity userAccount = userAccountRepository.findByUsername(username);
		if (userAccount == null) {
			throw new UsernameNotFoundException(String.format("User %s was not found", username));
		}
		return userAccount;
	}

	@Override
	public void saveIfNotExistsOrExpired(String username, String password)
			throws EntityExistsException {
		UserAccountEntity userAccount = userAccountRepository.findByUsername(username);
		if (userAccount != null) {
			if (userAccount.isActivationCodeExpired()) {
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
		UserAccountActivationCodeEntity activityCode = new UserAccountActivationCodeEntity();
		activityCode.setUserAccount(userAccount);
		userAccount.setActivationCode(activityCode);
		userAccountRepository.save(userAccount);
	}

	@Override
	public UserAccountEntity authenticateUserAndSetResponsenHeader(String username,
			String password, HttpServletResponse response)
			throws InvalidCredentialsException, InactiveAccountException {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(username,
						password));
		UserAccountEntity userAccount = userAccountRepository.findByUsername(username);		
		if (userAccount.isActivationCodeExpired()) {
			userAccountRepository.delete(userAccount);
			throw new InvalidCredentialsException();
		}
		if (!userAccount.isActive()) {
			throw new InactiveAccountException(String.format("User %s is inactive", username));
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

	@Override
	public void activateAccount(String username, String activationCode)
			throws AccountActiveException, BadActivationCodeException {
		UserAccountEntity userAccount = userAccountRepository.findByUsername(username);
		if (userAccount == null) {
			throw new UsernameNotFoundException(String.format("User %s was not found", username));
		} else if (userAccount.isActive()) {
			throw new AccountActiveException(String.format(
					"User %s is already active", username));
		}
		if (userAccount.isActivationCodeExpired()) {
			userAccountRepository.delete(userAccount);
		} else {
			if (!userAccount.getActivationCode().equals(activationCode)) {
				throw new BadActivationCodeException();
			}
			userAccount.activate();
			userAccountRepository.save(userAccount);
			userAccountActivationCodeRepository.deleteById(userAccount.getId());
		}
	}
}
