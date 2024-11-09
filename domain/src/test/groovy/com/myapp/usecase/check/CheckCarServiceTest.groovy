package com.myapp.usecase.check

import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import spock.lang.Specification

import static com.myapp.usecase.check.CheckCarFeature.ACCIDENT_FREE
import static com.myapp.usecase.check.CheckCarFeature.MAINTENANCE
import static com.myapp.usecase.check.MaintenanceFrequency.HIGH
import static java.util.Arrays.asList

class CheckCarServiceTest extends Specification {

  private final CheckCarRepository checkCarRepository = Mock()
  private final CheckCarService checkCarService = new CheckCarService(checkCarRepository)

  def "should check car with success"() {
    when:
    def checkCarQuery = new CheckCarQuery(vin, featuresToCheck)
    checkCarRepository.findNumberOfAccidents(vin) >> Mono.just(Optional.of(0))
    checkCarRepository.findMaintenanceFrequency(vin) >> Mono.just(Optional.of(HIGH))
    Mono<CheckCarResult> checkCarResult = checkCarService.check(checkCarQuery)

    then:
    StepVerifier.create(checkCarResult)
        .expectNext(expectedResult)
        .verifyComplete()

    where:
    vin    | featuresToCheck                              || expectedResult
    "1234" | features(asList(ACCIDENT_FREE, MAINTENANCE)) || new CheckCarResult(0, HIGH)
    "1234" | features(asList(ACCIDENT_FREE))              || new CheckCarResult(0, null)
    "1234" | features(asList(MAINTENANCE))                || new CheckCarResult(null, HIGH)
  }

  def features(List featureToCheck) {
    new HashSet<CheckCarFeature>(featureToCheck)
  }
}