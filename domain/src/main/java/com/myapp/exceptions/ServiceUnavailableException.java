package com.myapp.exceptions;

public class ServiceUnavailableException extends DomainException {

  public ServiceUnavailableException(String code, String message) {
    super(code, message);
  }
}
