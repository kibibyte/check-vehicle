package com.myapp.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class CheckCarExceptions {

  public static EntityNotFoundException entityNotFound() {
    var entityNotFound = ErrorCodes.ENTITY_NOT_FOUND;

    return new EntityNotFoundException(entityNotFound.name(), entityNotFound.getMessage());
  }

  public static InvalidArgumentException invalidArgument() {
    var invalidArgument = ErrorCodes.INVALID_ARGUMENT;

    return new InvalidArgumentException(invalidArgument.name(), invalidArgument.getMessage());
  }

  public static ServiceUnavailableException serviceUnavailableException() {
    var invalidArgument = ErrorCodes.SERVICE_UNAVAILABLE;

    return new ServiceUnavailableException(invalidArgument.name(), invalidArgument.getMessage());
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
