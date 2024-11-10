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
    final var vin = checkCarQuery.getVin();

    log.info("Check car requested");

    Mono<Integer> findNumberOfAccidents = checkRepository
        .findNumberOfAccidents(checkCarQuery.getVin())
        .doOnSubscribe(__ -> log.info("Find number of accidents requested [vin={}]", vin))
        .map(numberOfAccidents -> numberOfAccidents.orElseThrow(CheckCarExceptions::vinNotFound));

    Mono<MaintenanceFrequency> findMaintenanceFrequency = checkRepository
        .findMaintenanceFrequency(checkCarQuery.getVin())
        .doOnSubscribe(__ -> log.info("Find maintenance frequency requested [vin={}]", vin))
        .map(maintenanceFrequency -> maintenanceFrequency.orElseThrow(CheckCarExceptions::vinNotFound));

    if (checkCarQuery.isCheckAll()) {
      return Mono.zip(findNumberOfAccidents, findMaintenanceFrequency)
          .map(tuple -> new CheckCarResult(vin, tuple.getT1(), tuple.getT2()));
    }

    if (checkCarQuery.isCheckFeature(MAINTENANCE)) {
      return findMaintenanceFrequency.map(maintenanceFrequency -> CheckCarResult.of(vin, maintenanceFrequency));
    }

    if (checkCarQuery.isCheckFeature(ACCIDENT_FREE)) {
      return findNumberOfAccidents.map(numberOfAccidents -> CheckCarResult.of(vin, numberOfAccidents));
    }

    return Mono.empty();
  }
}
