package com.myapp.usecase.check;

import java.time.Duration;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Singleton
@AllArgsConstructor
class RestCheckCarRepository implements CheckCarRepository {

  private static final Logger log = LoggerFactory.getLogger(RestCheckCarRepository.class);

  private InsuranceClient insuranceClient;
  private MaintenanceClient maintenanceClient;

  @Override
  public Mono<Optional<Integer>> findNumberOfAccidents(String vin) {
    log.info("Find number of accidents requested");
    return insuranceClient.getReport(vin)
        .map(report -> Optional.ofNullable(
            report.getReport().getClaims())
        )
        .retryWhen(Retry.backoff(2, Duration.ofSeconds(2)).
            doBeforeRetry(signal -> {
              log.info(signal.toString());
            }))
        .onErrorResume(e -> {
          log.error(e.toString());
          return Mono.just(Optional.empty());
        });
  }

  @Override
  public Mono<Optional<MaintenanceFrequency>> findMaintenanceFrequency(String vin) {
    log.info("Find maintenance frequency requested");
    return maintenanceClient.getReport(vin)
        .map(report -> Optional.of(report.getMaintenanceFrequency()))
        .onErrorResume(e -> Mono.just(Optional.empty()));
  }
}
