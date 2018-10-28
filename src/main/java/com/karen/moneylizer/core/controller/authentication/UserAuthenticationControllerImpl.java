package com.karen.moneylizer.core.controller.authentication;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.karen.moneylizer.core.controller.authentication.dto.UserAccountLoginDto;
import com.karen.moneylizer.core.controller.authentication.dto.UserAccountRegisterationDto;
import com.karen.moneylizer.core.controller.authentication.dto.UserAccountRegisterationDtoValidator;
import com.karen.moneylizer.core.controller.authentication.dto.UserAccountResetDto;
import com.karen.moneylizer.core.controller.authentication.dto.UserAccountResetDtoValidator;
import com.karen.moneylizer.core.entity.userAccount.UserAccountEntity;
import com.karen.moneylizer.core.service.UserAccountService;
import com.karen.moneylizer.core.service.exceptions.AccountResetException;
import com.karen.moneylizer.core.service.exceptions.InvalidCredentialsException;
import com.karen.moneylizer.core.service.exceptions.UnconfirmedUsernameException;

@RestController
@RequestMapping("/api/authentication")
public class UserAuthenticationControllerImpl implements UserAuthenticationController {

	@Autowired
	private UserAccountService userAccountService;

	@Autowired
	private UserAccountRegisterationDtoValidator userAccountRegisterationDtoValidator;

	@Autowired
	private UserAccountResetDtoValidator userAccountResetDtoValidator;

	@Override
	public UserAccountEntity login(@RequestBody UserAccountLoginDto userAccountLoginDto,
			HttpServletResponse response) throws InvalidCredentialsException {
		return userAccountService.authenticateUserAndSetResponsenHeader(
				userAccountLoginDto.getUsername(), userAccountLoginDto.getPassword(), response);
	}

	@Override
	public UserAccountEntity create(@RequestBody UserAccountRegisterationDto userAccountRegisterationDto,
			HttpServletResponse response) throws EntityExistsException, InvalidCredentialsException {
		userAccountRegisterationDtoValidator.validate(userAccountRegisterationDto);
		String username = userAccountRegisterationDto.getUsername();
		String password = userAccountRegisterationDto.getPassword();
		userAccountService.saveIfNotExistsOrExpired(username, password);
		return userAccountService.authenticateUserAndSetResponsenHeader(
				username, password, response);
	}

	@Override
	public void triggerReset(@RequestParam(value = "username") String username) throws UnconfirmedUsernameException {
		userAccountService.triggerReset(username);
	}

	@Override
	public void doReset(@RequestBody UserAccountResetDto userAccount,
			@RequestParam(value = "token") String token) throws AccountResetException, InvalidCredentialsException {
		userAccountResetDtoValidator.validate(userAccount);
		userAccountService.doReset(userAccount, token);
	}

}