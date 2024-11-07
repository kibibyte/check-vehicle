package com.myapp.usecase.check;

import io.micronaut.serde.annotation.Serdeable;
import lombok.Builder;
import lombok.Value;

@Serdeable
@Value
@Builder
class CheckCarResponse {

  String requestId;
  String vin;
  Boolean accidentFree;
  MaintenanceScore maintenanceScore;
}
