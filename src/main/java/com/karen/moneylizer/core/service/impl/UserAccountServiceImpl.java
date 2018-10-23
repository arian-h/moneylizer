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
import com.karen.moneylizer.core.service.AccountActiveException;
import com.karen.moneylizer.core.service.AccountNotResetException;
import com.karen.moneylizer.core.service.InactiveAccountException;
import com.karen.moneylizer.core.service.InvalidActivationCodeException;
import com.karen.moneylizer.core.service.InvalidCredentialsException;
import com.karen.moneylizer.core.service.InvalidResetTokenException;
import com.karen.moneylizer.core.service.UserAccountService;
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
		UserAccountActivationCodeEntity activationCode = new UserAccountActivationCodeEntity();
		activationCode.setUserAccount(userAccount);
		userAccount.setActivationCode(activationCode);
		this.sendActivationCodeEmail(username, activationCode.getActivationCode());
		userAccountRepository.save(userAccount);
	}

	@Override
	public UserAccountEntity authenticateUserAndSetResponsenHeader(String username,
			String password, HttpServletResponse response)
			throws InvalidCredentialsException, InactiveAccountException {
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
			throw new InvalidCredentialsException();
		}
		userAccount.resetFailedLogin();
		if (userAccount.isActivationCodeExpired()) {
			userAccountRepository.delete(userAccount);
			throw new InvalidCredentialsException();
		} else if (!userAccount.isActive()) {
			throw new InactiveAccountException(username);
		} else if (userAccount.isReset()) {
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
			throws AccountActiveException, InvalidActivationCodeException {
		UserAccountEntity userAccount = this.loadUserByUsername(username);
		if (userAccount.isActive()) {
			throw new AccountActiveException(username);
		} else if (userAccount.isActivationCodeExpired()) {
			userAccountRepository.delete(userAccount);
		} else {
			if (!userAccount.getActivationCode().equals(activationCode)) {
				throw new InvalidActivationCodeException();
			}
			userAccount.activate();
			userAccountRepository.save(userAccount);
			userAccountActivationCodeRepository.deleteById(userAccount.getId());
		}
	}

	@Override
	public void doReset(String username) throws InactiveAccountException, InvalidCredentialsException{
		UserAccountEntity userAccount = this.loadUserByUsername(username);
		if (userAccount != null) {
			if (!userAccount.isActive()) {
				if (userAccount.isActivationCodeExpired()) {
					userAccountRepository.deleteById(userAccount.getId());
					throw new InvalidCredentialsException();
				}
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
			throws InvalidResetTokenException, InvalidCredentialsException,
			AccountNotResetException {
		String username = userAccountParam.getUsername();
		String password = userAccountParam.getPassword();
		UserAccountEntity userAccount = this.loadUserByUsername(username);
		if (!userAccount.isReset()) {
			throw new AccountNotResetException();
		} else if (!userAccount.getResetCodeValue().equals(resetCodeParam)) {
			throw new InvalidResetTokenException();
		}
		userAccount.resetFailedLogin();
		userAccount.setPassword(passwordEncoder.encode(password));
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
