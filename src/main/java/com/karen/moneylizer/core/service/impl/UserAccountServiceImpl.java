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
import com.karen.moneylizer.core.entity.userAccountResetCodeEntity.UserAccountResetCodeEntity;
import com.karen.moneylizer.core.entity.usernameConfirmationCode.UsernameConfirmationEntity;
import com.karen.moneylizer.core.repository.UsernameConfirmationRepository;
import com.karen.moneylizer.core.repository.UserAccountRepository;
import com.karen.moneylizer.core.repository.UserAccountResetCodeRepository;
import com.karen.moneylizer.core.service.UserAccountService;
import com.karen.moneylizer.core.service.exceptions.UnconfirmedUsernameException;
import com.karen.moneylizer.core.service.exceptions.UsernameConfirmationException;
import com.karen.moneylizer.core.service.exceptions.AccountResetException;
import com.karen.moneylizer.core.service.exceptions.InvalidCredentialsException;
import com.karen.moneylizer.emailServices.userAccountAuthentication.UsernameConfirmationEmail;
import com.karen.moneylizer.emailServices.userAccountAuthentication.UserAccountResetEmail;
import com.karen.moneylizer.security.SecurityConstants;

@Service
@Transactional
public class UserAccountServiceImpl implements UserAccountService {
	
	@Autowired
	private UserAccountRepository userAccountRepository;
	@Autowired
	private UsernameConfirmationRepository usernameConfirmationRepository;
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
		UsernameConfirmationEntity confirmationCode = new UsernameConfirmationEntity();
		confirmationCode.setUserAccount(userAccount);
		userAccount.setConfirmationCode(confirmationCode);
		this.sendConfirmationCodeEmail(username, confirmationCode.getConfirmationCode());
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
		if (userAccount.isReset()) {
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
	public void doConfirmUsername(String username, String confirmationCode)
			throws UsernameConfirmationException {
		UserAccountEntity userAccount = this.loadUserByUsername(username);
		if (userAccount.isUsernameConfirmed() || userAccount.isConfirmationCodeExpired() || !userAccount.getConfirmationCode().equals(confirmationCode)) {
			throw new UsernameConfirmationException(username);
		}
		userAccount.confirmUsername();
		userAccountRepository.save(userAccount);
		usernameConfirmationRepository.deleteById(userAccount.getId());
	}

	@Override
	public void triggerConfirmUsername(String username) throws UsernameConfirmationException {
		UserAccountEntity userAccount = this.loadUserByUsername(username);
		if (userAccount.isUsernameConfirmed()) {
			throw new UsernameConfirmationException(username);
		}
		userAccount.resetConfirmationCode();
		userAccountRepository.save(userAccount);
	}

	@Override
	public void triggerReset(String username) throws UnconfirmedUsernameException {
		UserAccountEntity userAccount = this.loadUserByUsername(username);
		if (userAccount != null) { //TODO: can we use Optional instead? 
			if (!userAccount.isUsernameConfirmed()) {
				throw new UnconfirmedUsernameException(username);
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
	public void doReset(UserAccountEntity userAccountParam, String resetCodeParam)
			throws AccountResetException, InvalidCredentialsException {
		UserAccountEntity userAccount = this.loadUserByUsername(userAccountParam.getUsername());
		if (userAccount == null) {
			throw new InvalidCredentialsException();
		}
		if (!userAccount.isReset() || !userAccount.getResetCodeValue().equals(resetCodeParam)) {
			throw new AccountResetException();
		}
		userAccount.resetFailedLogin();
		userAccount.setPassword(passwordEncoder.encode(userAccountParam.getPassword()));
		userAccount.setResetCode(null);
		userAccountRepository.save(userAccount);
		userAccountResetCodeRepository.deleteById(userAccount.getId());
	}

	private void sendConfirmationCodeEmail(String recipient, String confirmationCode) {
		UsernameConfirmationEmail email = new UsernameConfirmationEmail();
		email.setRecipients(new String[]{ recipient });
		email.setConfirmationCode(confirmationCode);
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
