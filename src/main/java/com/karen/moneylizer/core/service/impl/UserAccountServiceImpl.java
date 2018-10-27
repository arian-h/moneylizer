package com.karen.moneylizer.core.service.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.karen.moneylizer.RabbitMQConfiguration;
import com.karen.moneylizer.core.entity.user.UserEntity;
import com.karen.moneylizer.core.entity.userAccount.UserAccountEntity;
import com.karen.moneylizer.core.entity.userAccountActivationCode.UserAccountActivationCodeEntity;
import com.karen.moneylizer.core.entity.userAccountResetCodeEntity.UserAccountResetCodeEntity;
import com.karen.moneylizer.core.repository.UserAccountActivationCodeRepository;
import com.karen.moneylizer.core.repository.UserAccountRepository;
import com.karen.moneylizer.core.repository.UserAccountResetCodeRepository;
import com.karen.moneylizer.core.service.UserAccountService;
import com.karen.moneylizer.core.service.exceptions.InactiveAccountException;
import com.karen.moneylizer.core.service.exceptions.InvalidAccountActivationException;
import com.karen.moneylizer.core.service.exceptions.InvalidAccountResetActionException;
import com.karen.moneylizer.core.service.exceptions.InvalidCredentialsException;
import com.karen.moneylizer.emailServices.userAccountAuthentication.UserAccountActivationEmail;
import com.karen.moneylizer.emailServices.userAccountAuthentication.UserAccountResetEmail;
import com.karen.moneylizer.security.SecurityConstants;

@Service
@Transactional
public class UserAccountServiceImpl implements UserAccountService {
	
	@Autowired
	private UserAccountRepository userAccountRepository;
	@Autowired
	private UserAccountActivationCodeRepository userAccountActivationCodeRepository;
	@Autowired
	private UserAccountResetCodeRepository userAccountResetCodeRepository;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Override
	public UserAccountEntity loadUserByUsername(String username) {
		UserAccountEntity userAccount = userAccountRepository.findByUsername(username);
		if (userAccount == null) {
			throw new UsernameNotFoundException(String.format(
					"Username %s was not found", username));
		}
		return userAccount;
	}

	@Override
	public void saveIfNotExistsOrExpired(String username, String password)
			throws EntityExistsException {
		UserAccountEntity userAccount = userAccountRepository.findByUsername(username);
		if (userAccount != null) {
			throw new EntityExistsException(String.format(
					"Username %s is not available", username));
		}
		userAccount = new UserAccountEntity(username,
				passwordEncoder.encode(password));
		UserEntity user = new UserEntity();
		user.setUserAccount(userAccount);
		userAccount.setUser(user);
		UserAccountActivationCodeEntity activationCode = new UserAccountActivationCodeEntity();
		activationCode.setUserAccount(userAccount);
		userAccount.setActivationCode(activationCode);
		this.sendActivationCodeEmail(username, activationCode.getActivationCode());
		userAccountRepository.save(userAccount);
	}

	@Override
	public UserAccountEntity authenticateUserAndSetResponsenHeader(String username,
			String password, HttpServletResponse response)
			throws InvalidCredentialsException {
		Authentication authentication = null;
		UserAccountEntity userAccount = userAccountRepository.findByUsername(username);
		try {
			authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(username,
							password));
		} catch (BadCredentialsException exc) {
			if (userAccount != null) { // if account exists but the credentials were wrong
				userAccount.increaseFailedLogin();
				userAccountRepository.save(userAccount);				
			}
			throw new InvalidCredentialsException(exc);
		}
		userAccount.resetFailedLogin();
		if (userAccount.isReset()) { //TODO do we need this? when account is reset, it's already has a 
			throw new InvalidCredentialsException();
		}
		response.addHeader(SecurityConstants.AUTHENTICATION_HEADER, String
				.format("%s %s", SecurityConstants.BEARER,
						generateJwtToken(username)));
		SecurityContextHolder.getContext()
				.setAuthentication(authentication);
		return (UserAccountEntity) authentication.getPrincipal();
	}

	@Override
	public void activateAccount(String username, String activationCode)
			throws InvalidAccountActivationException {
		UserAccountEntity userAccount = this.loadUserByUsername(username);
		if (userAccount.isActive() || userAccount.isActivationCodeExpired() || !userAccount.getActivationCode().equals(activationCode)) {
			throw new InvalidAccountActivationException(username); // TODO can we change this to RuntimeException?
		}
		userAccount.activate();
		userAccountRepository.save(userAccount);
		userAccountActivationCodeRepository.deleteById(userAccount.getId());
	}

	@Override
	public void doReset(String username) throws InactiveAccountException {
		UserAccountEntity userAccount = this.loadUserByUsername(username);
		if (userAccount != null) { //TODO: can we use Optional instead? 
			if (!userAccount.isActive()) {
				throw new InactiveAccountException(username);
			}
			UserAccountResetCodeEntity resetCode = null;
			if (userAccount.isReset()) {
				resetCode = userAccount.getResetCode();
				resetCode.refreshResetCode();
			} else {
				resetCode = new UserAccountResetCodeEntity();
			}
			userAccount.setResetCode(resetCode);
			resetCode.setUserAccount(userAccount);
			userAccountRepository.save(userAccount);
			this.sendResetCodeEmail(username, resetCode.getResetCode());
		}
	}

	@Override
	public void reset(UserAccountEntity userAccountParam, String resetCodeParam)
			throws InvalidAccountResetActionException, InvalidCredentialsException {
		UserAccountEntity userAccount = this.loadUserByUsername(userAccountParam.getUsername());
		if (userAccount == null) {
			throw new InvalidCredentialsException();
		}
		if (!userAccount.isReset() || !userAccount.getResetCodeValue().equals(resetCodeParam)) {
			throw new InvalidAccountResetActionException();
		}
		userAccount.resetFailedLogin();
		userAccount.setPassword(passwordEncoder.encode(userAccountParam.getPassword()));
		userAccount.setResetCode(null);
		userAccountRepository.save(userAccount);
		userAccountResetCodeRepository.deleteById(userAccount.getId());
	}

	private void sendActivationCodeEmail(String recipient, String activationCode) {
		UserAccountActivationEmail email = new UserAccountActivationEmail();
		email.setRecipients(new String[]{ recipient });
		email.setActivationCode(activationCode);
		rabbitTemplate.convertAndSend(RabbitMQConfiguration.AUTHENTICATION_EMAILS_QUEUE, email);
	}

	private void sendResetCodeEmail(String recipient, String resetCode) {
		UserAccountResetEmail email = new UserAccountResetEmail();
		email.setRecipients(new String[]{ recipient });
		email.setResetCode(resetCode);
		rabbitTemplate.convertAndSend(RabbitMQConfiguration.AUTHENTICATION_EMAILS_QUEUE, email);
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
