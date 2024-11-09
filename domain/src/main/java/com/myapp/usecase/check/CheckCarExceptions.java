package com.myapp.usecase.check;

import com.myapp.exceptions.EntityNotFoundException;
import com.myapp.exceptions.InvalidArgumentException;
import com.myapp.exceptions.RestRepositoryException;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class CheckCarExceptions {

  public static EntityNotFoundException entityNotFound() {
    var code = ErrorCodes.ENTITY_NOT_FOUND;

    return new EntityNotFoundException(code.name(), code.getMessage());
  }

  public static InvalidArgumentException invalidArgument() {
    var code = ErrorCodes.INVALID_ARGUMENT;

    return new InvalidArgumentException(code.name(), code.getMessage());
  }

  public static RestRepositoryException restRepositoryException() {
    var code = ErrorCodes.SERVICE_UNAVAILABLE;

    return new RestRepositoryException(code.name(), code.getMessage());
  }

  @AllArgsConstructor
  @Getter
  public enum ErrorCodes {
    ENTITY_NOT_FOUND("Vin not found"),
    INVALID_ARGUMENT("Invalid argument"),
    SERVICE_UNAVAILABLE("Service unavailable");

    private final String message;
  }
}
