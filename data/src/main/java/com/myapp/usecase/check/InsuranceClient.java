package com.myapp.usecase.check;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Value;
import reactor.core.publisher.Mono;

@Client(id = "insurance")
interface InsuranceClient {

  @Get("/accidents/report?vin={vin}")
  Mono<InsuranceReport> getReport(@QueryValue String vin);

  @Serdeable
  @Value
  class InsuranceReport {

    Report report;

    Integer getClaims() {
      return report.claims;
    }

    @Serdeable
    @Value
    static class Report {

      Integer claims;
    }
  }
}
