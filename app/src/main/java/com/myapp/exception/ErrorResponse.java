package com.myapp.exception;

import com.myapp.exceptions.DomainException;

import io.micronaut.serde.annotation.Serdeable;
import lombok.Value;

@Serdeable
@Value
class ErrorResponse {

  String code;
  String message;

  static ErrorResponse of(DomainException exception) {
    return new ErrorResponse(exception.getCode(), exception.getMessage());
  }

  static ErrorResponse of(String code, String message) {
    return new ErrorResponse(code, message);
  }
}
