package com.myapp.exception;

import com.myapp.exceptions.DomainException;
import com.myapp.exceptions.EntityNotFoundException;
import com.myapp.exceptions.InvalidArgumentException;
import com.myapp.exceptions.RestRepositoryException;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

@Produces
@Singleton
class DomainExceptionHandler implements ExceptionHandler<DomainException, HttpResponse<ErrorResponse>> {

  @Override
  public HttpResponse<ErrorResponse> handle(HttpRequest request, DomainException e) {
    if (e instanceof EntityNotFoundException) {
      return HttpResponse.notFound(ErrorResponse.of(e.getCode(), e.getMessage()));
    }
    if (e instanceof InvalidArgumentException) {
      return HttpResponse.notFound(ErrorResponse.of(e.getCode(), e.getMessage()));
    }
    if (e instanceof RestRepositoryException) {
      return HttpResponse.status(HttpStatus.SERVICE_UNAVAILABLE);
    }

    return HttpResponse.serverError(ErrorResponse.of(e.getCode(), e.getMessage()));
  }
}