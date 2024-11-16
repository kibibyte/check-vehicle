package com.myapp.usecase.check;

import static com.myapp.usecase.check.CheckCarFeature.ACCIDENT_FREE;
import static com.myapp.usecase.check.CheckCarFeature.MAINTENANCE;

import java.util.Optional;

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

    Mono<Optional<Integer>> findNumberOfAccidents = checkCarRepository
        .findNumberOfAccidents(checkCarQuery.getVin())
        .doOnSubscribe(__ -> log.info("Find number of accidents requested"));

    Mono<Optional<MaintenanceFrequency>> findMaintenanceFrequency = checkCarRepository
        .findMaintenanceFrequency(checkCarQuery.getVin())
        .doOnSubscribe(__ -> log.info("Find maintenance frequency requested"));

    Mono<Tuple2<Optional<Integer>, Optional<MaintenanceFrequency>>> findFeatures = Mono.zip(
        checkCarQuery.isCheckFeature(ACCIDENT_FREE) ? findNumberOfAccidents : Mono.just(Optional.empty()),
        checkCarQuery.isCheckFeature(MAINTENANCE) ? findMaintenanceFrequency : Mono.just(Optional.empty())
    );

    return findFeatures.map(tuple -> new CheckCarResult(
        vin,
        tuple.getT1().orElse(null),
        tuple.getT2().orElse(null))
    );
  }
}
