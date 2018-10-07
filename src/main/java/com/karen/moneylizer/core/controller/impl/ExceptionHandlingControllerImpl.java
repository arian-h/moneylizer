package com.karen.moneylizer.core.controller.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.karen.moneylizer.core.controller.ExceptionHandlingController;
import com.karen.moneylizer.core.controller.ExceptionResponse;

@ControllerAdvice
public class ExceptionHandlingControllerImpl implements ExceptionHandlingController {
	@Override
	public ResponseEntity<ExceptionResponse> invalidInput(Exception ex) {
		return createErrorResponse(ex.getMessage());
	}

	@Override
	public ResponseEntity<ExceptionResponse> invalidInput(MismatchedInputException ex) {
		return createErrorResponse(((MismatchedInputException) ex).getOriginalMessage());
	}

	@Override
	public ResponseEntity<ExceptionResponse> invalidInput(UsernameNotFoundException ex) {
		return createErrorResponse(((UsernameNotFoundException) ex).getMessage());
	}

	@Override
	public ResponseEntity<ExceptionResponse> invalidInput(MethodArgumentNotValidException ex) {
		return createErrorResponse(ex.getBindingResult().getAllErrors().get(0).getCode());
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
