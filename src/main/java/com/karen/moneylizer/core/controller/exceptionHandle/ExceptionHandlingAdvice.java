package com.karen.moneylizer.core.controller.exceptionHandle;

import javax.persistence.EntityExistsException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.karen.moneylizer.core.service.exceptions.InactiveAccountException;
import com.karen.moneylizer.core.service.exceptions.InvalidAccountActivationException;
import com.karen.moneylizer.core.service.exceptions.InvalidAccountResetActionException;
import com.karen.moneylizer.core.service.exceptions.InvalidCredentialsException;


public interface ExceptionHandlingAdvice {

	@ExceptionHandler(value = { EntityExistsException.class,
			UsernameNotFoundException.class, BadCredentialsException.class,
			InvalidCredentialsException.class,
			InvalidAccountResetActionException.class, InvalidAccountActivationException.class,
			InactiveAccountException.class,
			LockedException.class})
	public ResponseEntity<ExceptionResponse> invalidInput(Exception ex);

	@ExceptionHandler(value = { MismatchedInputException.class })
	public ResponseEntity<ExceptionResponse> invalidInput(MismatchedInputException ex);

	@ExceptionHandler(value = { MethodArgumentNotValidException.class })
	public ResponseEntity<ExceptionResponse> invalidInput(MethodArgumentNotValidException ex);

}