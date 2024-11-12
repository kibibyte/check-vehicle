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
  private final CheckCarRepository checkCarRepository;

  Mono<CheckCarResult> check(CheckCarQuery checkCarQuery) {
    final String vin = checkCarQuery.getVin();
    log.info("Check car requested");

    Mono<Integer> getNumberOfAccidents = checkCarRepository
        .findNumberOfAccidents(checkCarQuery.getVin())
        .map(numberOfAccidents -> numberOfAccidents.orElseThrow(CheckCarExceptions::vinNotFound))
        .doOnSubscribe(__ -> log.info("Find number of accidents requested"));

    Mono<MaintenanceFrequency> getMaintenanceFrequency = checkCarRepository
        .findMaintenanceFrequency(checkCarQuery.getVin())
        .map(maintenanceFrequency -> maintenanceFrequency.orElseThrow(CheckCarExceptions::vinNotFound))
        .doOnSubscribe(__ -> log.info("Find maintenance frequency requested"));

    if (checkCarQuery.isCheckAll()) {
      return Mono.zip(getNumberOfAccidents, getMaintenanceFrequency)
          .map(tuple -> new CheckCarResult(vin, tuple.getT1(), tuple.getT2()));
    }

    if (checkCarQuery.isCheckFeature(ACCIDENT_FREE)) {
      return getNumberOfAccidents.map(numberOfAccidents -> CheckCarResult.of(vin, numberOfAccidents));
    }

    if (checkCarQuery.isCheckFeature(MAINTENANCE)) {
      return getMaintenanceFrequency.map(maintenanceFrequency -> CheckCarResult.of(vin, maintenanceFrequency));
    }

    return Mono.empty();
  }
}
