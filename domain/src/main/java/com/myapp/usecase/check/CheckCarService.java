package com.myapp.usecase.check;

import static com.myapp.usecase.check.CheckCarFeature.ACCIDENT_FREE;
import static com.myapp.usecase.check.CheckCarFeature.MAINTENANCE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Singleton
@AllArgsConstructor
class CheckCarService {

  private static final Logger log = LoggerFactory.getLogger(CheckCarService.class);
  private final CheckCarRepository checkRepository;

  Mono<CheckCarResult> check(CheckCarQuery checkCarQuery) {
    log.info("Check car requested");

    Mono<Integer> numberOfAccidents = checkRepository
        .findNumberOfAccidents(checkCarQuery.getVin())
        .doOnSubscribe(s -> log.info("Find number of accidents requested"))
        .flatMap(value -> Mono.just(value.orElseThrow(CheckCarExceptions::entityNotFound)));

    Mono<MaintenanceFrequency> maintenanceFrequency = checkRepository
        .findMaintenanceFrequency(checkCarQuery.getVin())
        .doOnSubscribe(s -> log.info("Find maintenance frequency requested"))
        .flatMap(value -> Mono.just(value.orElseThrow(CheckCarExceptions::entityNotFound)));

    if (checkCarQuery.isCheckAll()) {
      return Mono.zip(numberOfAccidents, maintenanceFrequency)
          .flatMap(tuple -> Mono.just(new CheckCarResult(checkCarQuery.getVin(), tuple.getT1(), tuple.getT2())));
    }

    if (checkCarQuery.isCheckFeature(MAINTENANCE)) {
      return maintenanceFrequency.flatMap((value) -> Mono.just(CheckCarResult.of(checkCarQuery.getVin(), value)));
    }

    if (checkCarQuery.isCheckFeature(ACCIDENT_FREE)) {
      return numberOfAccidents.flatMap((value) -> Mono.just(CheckCarResult.of(checkCarQuery.getVin(), value)));
    }

    return Mono.empty();
  }
}
