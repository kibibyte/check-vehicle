package com.myapp.exception;

import com.myapp.exceptions.DomainException;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

@Produces
@Singleton
class DomainExceptionHandler implements ExceptionHandler<DomainException, HttpResponse<ErrorResponse>> {

  @Override
  public HttpResponse<ErrorResponse> handle(HttpRequest request, DomainException e) {
    return HttpResponse.badRequest(ErrorResponse.of(e.getCode(), e.getMessage()));
  }
}