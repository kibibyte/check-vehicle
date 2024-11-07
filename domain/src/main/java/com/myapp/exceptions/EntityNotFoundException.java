package com.myapp.exceptions;

public class EntityNotFoundException extends DomainException {

  public EntityNotFoundException(String code, String message) {
    super(code, message);
  }
}
