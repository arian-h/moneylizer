package com.karen.moneylizer.core.controller;

import javax.persistence.EntityExistsException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.karen.moneylizer.core.service.AccountActiveException;
import com.karen.moneylizer.core.service.InactiveAccountException;
import com.karen.moneylizer.core.service.InvalidCredentialsException;


public interface ExceptionHandlingController {

	@ExceptionHandler(value = { EntityExistsException.class,
			BadCredentialsException.class, InvalidCredentialsException.class })
	public ResponseEntity<ExceptionResponse> invalidInput(Exception ex);

	@ExceptionHandler(value = { MismatchedInputException.class })
	public ResponseEntity<ExceptionResponse> invalidInput(MismatchedInputException ex);

	@ExceptionHandler(value = { UsernameNotFoundException.class })
	public ResponseEntity<ExceptionResponse> invalidInput(UsernameNotFoundException ex);

	@ExceptionHandler(value = { MethodArgumentNotValidException.class })
	public ResponseEntity<ExceptionResponse> invalidInput(MethodArgumentNotValidException ex);

	@ExceptionHandler(value = { InactiveAccountException.class })
	public ResponseEntity<ExceptionResponse> invalidInput(InactiveAccountException ex);

	@ExceptionHandler(value = {AccountActiveException.class})
	public ResponseEntity<ExceptionResponse> invalidInput(AccountActiveException ex);
}