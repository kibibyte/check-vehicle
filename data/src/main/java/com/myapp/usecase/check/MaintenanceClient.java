package com.myapp.usecase.check;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Value;
import reactor.core.publisher.Mono;

@Client(id = "maintenance")
interface MaintenanceClient {

  @Get("/cars/{vin}")
  Mono<MaintenanceReport> getReport(@PathVariable String vin);

  @Serdeable
  @Value
  class MaintenanceReport {

    @JsonProperty("maintenance_frequency")
    MaintenanceFrequency maintenanceFrequency;
  }
}
