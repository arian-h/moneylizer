package com.karen.moneylizer.core.controller;

import javax.persistence.EntityExistsException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.karen.moneylizer.core.service.InactiveAccountException;

@ControllerAdvice
public class ExceptionHandlingController {

	@ExceptionHandler(value = { EntityExistsException.class, BadCredentialsException.class })
	public ResponseEntity<ExceptionResponse> invalidInput(Exception ex) {
		return createErrorResponse(ex.getMessage());
	}

	@ExceptionHandler(value = { MismatchedInputException.class })
	public ResponseEntity<ExceptionResponse> invalidInput(MismatchedInputException ex) {
		return createErrorResponse(((MismatchedInputException) ex).getOriginalMessage());
	}

	@ExceptionHandler(value = { MethodArgumentNotValidException.class })
	public ResponseEntity<ExceptionResponse> invalidInput(MethodArgumentNotValidException ex) {
		return createErrorResponse(ex.getBindingResult().getAllErrors().get(0).getCode());
	}

	@ExceptionHandler(value = { InactiveAccountException.class })
	public ResponseEntity<ExceptionResponse> invalidInput(InactiveAccountException ex) {
		return createErrorResponse(ex.getMessage());
	}

	private ResponseEntity<ExceptionResponse> createErrorResponse(
			String errorMessage) {
		ExceptionResponse response = new ExceptionResponse();
		response.setErrorCode("BAD_REQUEST");
		response.setErrorMessage(errorMessage);
		return new ResponseEntity<ExceptionResponse>(response,
				HttpStatus.BAD_REQUEST);
	}
}