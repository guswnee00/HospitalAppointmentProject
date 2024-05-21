package org.zerobase.hospitalappointmentproject.global.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

  @ExceptionHandler(CustomException.class)
  protected ErrorResponse handleCustomException(CustomException e) {

    return new ErrorResponse(e.getErrorCode());

  }

}
