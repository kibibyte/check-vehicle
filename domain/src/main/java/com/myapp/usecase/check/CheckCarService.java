package com.myapp.usecase.check;

import static com.myapp.usecase.check.CheckCarFeature.ACCIDENT_FREE;
import static com.myapp.usecase.check.CheckCarFeature.MAINTENANCE;
import static java.util.Arrays.asList;

import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Singleton
@AllArgsConstructor
class CheckCarService {

  private final CheckCarRepository checkRepository;

  Mono<CheckCarResult> check(CheckCarQuery checkCarQuery) {
    var getNumberOfAccidentsMono = checkRepository.findNumberOfAccidents(checkCarQuery.getVin());
    var getMaintenanceFrequencyMono = checkRepository.findMaintenanceFrequency(checkCarQuery.getVin());

    if (checkCarQuery.getFeaturesToCheck().containsAll(asList(ACCIDENT_FREE, MAINTENANCE))) {
      return Mono.zip(getNumberOfAccidentsMono, getMaintenanceFrequencyMono)
          .map(tuple -> new CheckCarResult(
              tuple.getT1().orElse(null),
              tuple.getT2().orElse(null)
          ));
    }

    if (checkCarQuery.getFeaturesToCheck().contains(MAINTENANCE)) {
      return getMaintenanceFrequencyMono
          .map(maintenanceFrequency -> new CheckCarResult(null, maintenanceFrequency.orElse(null)));
    }

    if (checkCarQuery.getFeaturesToCheck().contains(ACCIDENT_FREE)) {
      return getNumberOfAccidentsMono
          .map(numberOfAccidents -> new CheckCarResult(numberOfAccidents.orElse(null), null));
    }

    return Mono.empty();
  }
}
