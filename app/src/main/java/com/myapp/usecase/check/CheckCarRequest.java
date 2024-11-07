package com.myapp.usecase.check;

import java.util.Set;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Serdeable
@Value
class CheckCarRequest {

  @NotBlank
  String vin;

  @NotNull
  @Size(min = 1, max = 2)
  Set<CheckCarFeature> features;
}
