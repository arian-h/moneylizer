package com.karen.moneylizer.core.controller;

import javax.persistence.EntityExistsException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;

@ControllerAdvice
public class ExceptionHandlingController {

	@ExceptionHandler(value = { MethodArgumentNotValidException.class,
			EntityExistsException.class, BadCredentialsException.class, MismatchedInputException.class })
	public ResponseEntity<ExceptionResponse> invalidInput(Exception ex) {
		ExceptionResponse response = new ExceptionResponse();
		response.setErrorCode("BAD_REQUEST");
		response.setErrorMessage(ex.getMessage());
		return new ResponseEntity<ExceptionResponse>(response,
				HttpStatus.BAD_REQUEST);
	}

}