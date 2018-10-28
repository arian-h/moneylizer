package com.karen.moneylizer.core.controller.exceptionHandler;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionResponse {

    private String errorCode;
    private String errorMessage;

}