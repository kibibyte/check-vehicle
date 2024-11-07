package com.myapp.exceptions;

import lombok.Getter;

@Getter
public class DomainException extends RuntimeException {

  private final String code;

  public DomainException(String code, String message) {
    super(message);
    this.code = code;
  }
}
