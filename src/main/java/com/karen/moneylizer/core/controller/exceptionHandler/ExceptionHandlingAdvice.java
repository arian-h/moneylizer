package com.karen.moneylizer.core.controller.exceptionHandler;

import javax.persistence.EntityExistsException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.karen.moneylizer.core.service.exceptions.UnconfirmedUsernameException;
import com.karen.moneylizer.core.service.exceptions.UsernameConfirmationException;
import com.karen.moneylizer.core.service.exceptions.AccountResetException;
import com.karen.moneylizer.core.service.exceptions.InvalidCredentialsException;


public interface ExceptionHandlingAdvice {

	@ExceptionHandler(value = { EntityExistsException.class,
			UsernameNotFoundException.class, BadCredentialsException.class,
			InvalidCredentialsException.class,
			AccountResetException.class, UsernameConfirmationException.class,
			UnconfirmedUsernameException.class,
			LockedException.class, IllegalArgumentException.class})
	public ResponseEntity<ExceptionResponse> invalidInput(Exception ex);

	@ExceptionHandler(value = { MismatchedInputException.class })
	public ResponseEntity<ExceptionResponse> invalidInput(MismatchedInputException ex);

	@ExceptionHandler(value = { MethodArgumentNotValidException.class })
	public ResponseEntity<ExceptionResponse> invalidInput(MethodArgumentNotValidException ex);

}