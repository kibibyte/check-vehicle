package com.myapp.exceptions;

public class InvalidArgumentException extends DomainException {

  public InvalidArgumentException(String code, String message) {
    super(code, message);
  }
}