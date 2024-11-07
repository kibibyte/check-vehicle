package com.myapp.usecase.check;

import java.util.Optional;

import reactor.core.publisher.Mono;

interface CheckCarRepository {

  Mono<Optional<Integer>> findNumberOfAccidents(String vin);

  Mono<Optional<MaintenanceFrequency>> findMaintenanceFrequency(String vin);
}
