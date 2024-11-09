package com.myapp.usecase.check;

import static io.micronaut.http.HttpStatus.NOT_FOUND;
import static java.time.Duration.ofSeconds;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.http.client.exceptions.HttpClientResponseException;
import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

@Singleton
@AllArgsConstructor
class RestCheckCarRepository implements CheckCarRepository {

  private static final Logger log = LoggerFactory.getLogger(RestCheckCarRepository.class);

  private InsuranceClient insuranceClient;
  private MaintenanceClient maintenanceClient;

  private static final int RETRY_MAX_ATTEMPTS = 2;
  private static final int RETRY_DELAY_SEC = 2;

  @Override
  public Mono<Optional<Integer>> findNumberOfAccidents(String vin) {
    return insuranceClient.getReport(vin)
        .flatMap(report -> Mono.just(Optional.of(report.getClaims())))
        .retryWhen(getRetrySpec())
        .onErrorResume((e -> {
          log.error("Cannot get number of accidents", e);
          return is404Error(e) ? Mono.just(Optional.empty())
              : Mono.error(CheckCarExceptions::restRepositoryException);
        }));
  }

  @Override
  public Mono<Optional<MaintenanceFrequency>> findMaintenanceFrequency(String vin) {
    return maintenanceClient.getReport(vin)
        .flatMap(report -> Mono.just(Optional.of(report.getMaintenanceFrequency())))
        .retryWhen(getRetrySpec())
        .onErrorResume((e -> {
          log.error("Cannot get maintenance frequency", e);
          return is404Error(e) ? Mono.just(Optional.empty())
              : Mono.error(CheckCarExceptions::restRepositoryException);
        }));
  }

  private static RetryBackoffSpec getRetrySpec() {
    return Retry
        .backoff(RETRY_MAX_ATTEMPTS, ofSeconds(RETRY_DELAY_SEC))
        .filter(e -> !is404Error(e));
  }

  private static boolean is404Error(Throwable e) {
    if (e instanceof HttpClientResponseException hcre) {
      return hcre.getStatus().equals(NOT_FOUND);
    }
    return false;
  }
}
