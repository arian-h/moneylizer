package com.karen.moneylizer.core.controller.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;

@ControllerAdvice
public class ExceptionHandlingAdviceImpl implements ExceptionHandlingAdvice {
	@Override
	public ResponseEntity<ExceptionResponse> invalidInput(Exception ex) {
		return createErrorResponse(ex.getMessage());
	}

	@Override
	public ResponseEntity<ExceptionResponse> invalidInput(MismatchedInputException ex) {
		return createErrorResponse(((MismatchedInputException) ex).getOriginalMessage());
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
