package com.myapp.usecase.check;

import static io.micronaut.http.MediaType.APPLICATION_JSON;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Controller
@AllArgsConstructor
class CheckCarController {

  private static final Logger log = LoggerFactory.getLogger(CheckCarController.class);
  private final CheckCarService checkCarService;

  @Post(value = "/check", consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
  Mono<CheckCarResponse> check(@Valid @Body CheckCarRequest request) {
    log.info("Check car requested");
    var checkCar = new CheckCarQuery(request.getVin(), request.getFeatures());

    return checkCarService.check(checkCar)
        .map(checkCarResult -> new CheckCarResponse(
            MDC.get("requestId"),
            request.getVin(),
            checkCarResult.getAccidentFree().orElse(null),
            checkCarResult.getMaintenanceScore().orElse(null))
        );
  }
}