package com.myapp.usecase.check;

import static com.myapp.usecase.check.CheckCarFeature.ACCIDENT_FREE;
import static com.myapp.usecase.check.CheckCarFeature.MAINTENANCE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Singleton
@AllArgsConstructor
class CheckCarService {

  private static final Logger log = LoggerFactory.getLogger(CheckCarService.class);
  private final CheckCarRepository checkCarRepository;

  Mono<CheckCarResult> check(CheckCarQuery checkCarQuery) {
    final String vin = checkCarQuery.getVin();
    log.info("Check car requested");

    Mono<Integer> checkNumberOfAccidents = checkCarRepository
        .findNumberOfAccidents(checkCarQuery.getVin())
        .map(numberOfAccidents -> numberOfAccidents.orElseThrow(CheckCarExceptions::vinNotFound))
        .doOnSubscribe(__ -> log.info("Find number of accidents requested"));

    Mono<MaintenanceFrequency> checkMaintenanceFrequency = checkCarRepository
        .findMaintenanceFrequency(checkCarQuery.getVin())
        .map(maintenanceFrequency -> maintenanceFrequency.orElseThrow(CheckCarExceptions::vinNotFound))
        .doOnSubscribe(__ -> log.info("Find maintenance frequency requested"));

    Mono<Tuple2<Integer, MaintenanceFrequency>> checkAllFeatures = Mono.zip(
        checkNumberOfAccidents,
        checkMaintenanceFrequency
    );

    if (checkCarQuery.isCheckAll()) {
      return checkAllFeatures
          .map(tuple -> new CheckCarResult(vin, tuple.getT1(), tuple.getT2()));
    }

    if (checkCarQuery.isCheckFeature(ACCIDENT_FREE)) {
      return checkNumberOfAccidents
          .map(numberOfAccidents -> CheckCarResult.of(vin, numberOfAccidents));
    }

    if (checkCarQuery.isCheckFeature(MAINTENANCE)) {
      return checkMaintenanceFrequency
          .map(maintenanceFrequency -> CheckCarResult.of(vin, maintenanceFrequency));
    }

    return Mono.empty();
  }
}
