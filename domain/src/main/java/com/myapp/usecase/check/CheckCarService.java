package com.myapp.usecase.check;

import static com.myapp.usecase.check.CheckCarFeature.ACCIDENT_FREE;
import static com.myapp.usecase.check.CheckCarFeature.MAINTENANCE;
import static java.util.Arrays.asList;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myapp.exceptions.CheckCarExceptions;

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

    Mono<Integer> numberOfAccidents = getNumberOfAccidents(checkCarQuery)
        .flatMap(value -> Mono.just(value.orElseThrow(CheckCarExceptions::entityNotFound)));

    Mono<MaintenanceFrequency> maintenanceFrequency = getMaintenanceFrequency(checkCarQuery)
        .flatMap(value -> Mono.just(value.orElseThrow(CheckCarExceptions::entityNotFound)));

    if (checkCarQuery.getFeaturesToCheck().containsAll(asList(ACCIDENT_FREE, MAINTENANCE))) {
      return Mono.zip(numberOfAccidents, maintenanceFrequency).
          flatMap(tuple -> Mono.just(new CheckCarResult(tuple.getT1(), tuple.getT2())));
    }

    if (checkCarQuery.getFeaturesToCheck().contains(MAINTENANCE)) {
      return maintenanceFrequency.flatMap((value) -> Mono.just(CheckCarResult.of(value)));
    }

    if (checkCarQuery.getFeaturesToCheck().contains(ACCIDENT_FREE)) {
      return numberOfAccidents.flatMap((value) -> Mono.just(CheckCarResult.of(value)));
    }

    return Mono.empty();
  }

  private Mono<Optional<Integer>> getNumberOfAccidents(CheckCarQuery checkCarQuery) {
    return checkRepository.findNumberOfAccidents(checkCarQuery.getVin())
        .doOnSubscribe(s -> log.info("Find number of accidents requested"));
  }

  private Mono<Optional<MaintenanceFrequency>> getMaintenanceFrequency(CheckCarQuery checkCarQuery) {
    return checkRepository.findMaintenanceFrequency(checkCarQuery.getVin())
        .doOnSubscribe(s -> log.info("Find maintenance frequency requested"));
  }
}
