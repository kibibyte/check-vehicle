package com.myapp.exceptions;

public class RestRepositoryException extends DomainException {

  public RestRepositoryException(String code, String message) {
    super(code, message);
  }
}
