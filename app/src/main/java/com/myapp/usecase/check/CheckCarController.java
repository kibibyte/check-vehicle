package com.myapp.usecase.check;

import static io.micronaut.http.MediaType.APPLICATION_JSON;

import org.slf4j.MDC;

import com.myapp.filters.MDCFilter;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Controller
@AllArgsConstructor
class CheckCarController {

  private final CheckCarService checkCarService;

  @Post(value = "/check", consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
  Mono<CheckCarResponse> check(@Valid @Body CheckCarRequest request) {
    var checkCar = new CheckCarQuery(request.getVin(), request.getFeatures());

    return checkCarService.check(checkCar)
        .map(this::toCheckCarResponse);
  }

  private CheckCarResponse toCheckCarResponse(CheckCarResult result) {
    return CheckCarResponse.builder()
        .requestId(MDC.get(MDCFilter.REQUEST_ID))
        .vin(result.getVin())
        .accidentFree(result.getAccidentFree().orElse(null))
        .maintenanceScore(result.getMaintenanceScore().orElse(null))
        .build();
  }
}