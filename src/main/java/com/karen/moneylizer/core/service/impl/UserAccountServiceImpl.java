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
import com.karen.moneylizer.core.entity.userAccountResetCodeEntity.UserAccountResetCodeEntity;
import com.karen.moneylizer.core.repository.UserAccountActivationCodeRepository;
import com.karen.moneylizer.core.repository.UserAccountRepository;
import com.karen.moneylizer.core.service.AccountActiveException;
import com.karen.moneylizer.core.service.AccountNotResetException;
import com.karen.moneylizer.core.service.InvalidActivationCodeException;
import com.karen.moneylizer.core.service.InactiveAccountException;
import com.karen.moneylizer.core.service.InvalidCredentialsException;
import com.karen.moneylizer.core.service.InvalidResetTokenException;
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
	public void triggerReset(String username) throws InactiveAccountException{
		UserAccountEntity userAccount = this.loadUserByUsername(username);
		if (!userAccount.isActive()) {
			throw new InactiveAccountException(username);
		}
		UserAccountResetCodeEntity resetCode = new UserAccountResetCodeEntity();
		userAccount.setResetCode(resetCode);
		resetCode.setUserAccount(userAccount);
		userAccountRepository.save(userAccount);
		// TODO send an email to the user with a link (including username and
		// reset code) to the password reset page
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
		} else if (userAccount.getResetCode() != resetCodeParam) {
			throw new InvalidResetTokenException();
		} else if (userAccount.getPassword() != passwordEncoder
				.encode(password)) {
			throw new InvalidCredentialsException();
		}
		userAccount.setPassword(passwordEncoder.encode(password));
		userAccountRepository.save(userAccount);
	}
}
