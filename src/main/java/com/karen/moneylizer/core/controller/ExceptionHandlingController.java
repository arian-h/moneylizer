package com.karen.moneylizer.core.controller;

import javax.persistence.EntityExistsException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.karen.moneylizer.core.service.AccountActiveException;
import com.karen.moneylizer.core.service.AccountNotResetException;
import com.karen.moneylizer.core.service.InvalidActivationCodeException;
import com.karen.moneylizer.core.service.InactiveAccountException;
import com.karen.moneylizer.core.service.InvalidCredentialsException;
import com.karen.moneylizer.core.service.InvalidResetTokenException;


public interface ExceptionHandlingController {

	@ExceptionHandler(value = { EntityExistsException.class,
			UsernameNotFoundException.class, BadCredentialsException.class,
			InvalidCredentialsException.class,
			InvalidResetTokenException.class, AccountActiveException.class,
			InactiveAccountException.class,
			InvalidActivationCodeException.class, 
			AccountNotResetException.class, 
			LockedException.class})
	public ResponseEntity<ExceptionResponse> invalidInput(Exception ex);

	@ExceptionHandler(value = { MismatchedInputException.class })
	public ResponseEntity<ExceptionResponse> invalidInput(MismatchedInputException ex);

	@ExceptionHandler(value = { MethodArgumentNotValidException.class })
	public ResponseEntity<ExceptionResponse> invalidInput(MethodArgumentNotValidException ex);

}