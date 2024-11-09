package com.myapp.usecase.check;

import static com.myapp.usecase.check.MaintenanceFrequency.LOW;
import static com.myapp.usecase.check.MaintenanceFrequency.MEDIUM;
import static com.myapp.usecase.check.MaintenanceFrequency.VERY_LOW;
import static com.myapp.usecase.check.MaintenanceScore.AVERAGE;
import static com.myapp.usecase.check.MaintenanceScore.GOOD;
import static com.myapp.usecase.check.MaintenanceScore.POOR;
import static java.util.Optional.ofNullable;

import java.util.Optional;

import lombok.Value;

@Value
class CheckCarResult {

  Boolean accidentFree;
  MaintenanceScore maintenanceScore;

  CheckCarResult(Integer numberOfAccidents, MaintenanceFrequency maintenanceFrequency) {
    this.accidentFree = ofNullable(numberOfAccidents)
        .map(_numberOfAccidents -> _numberOfAccidents.equals(0))
        .orElse(null);
    this.maintenanceScore = ofNullable(maintenanceFrequency)
        .map(this::mapToMaintenanceScore)
        .orElse(null);
  }

  Optional<Boolean> getAccidentFree() {
    return ofNullable(accidentFree);
  }

  Optional<MaintenanceScore> getMaintenanceScore() {
    return ofNullable(maintenanceScore);
  }

  static CheckCarResult of(Integer numberOfAccidents) {
    return new CheckCarResult(numberOfAccidents, null);
  }

  static CheckCarResult of(MaintenanceFrequency maintenanceFrequency) {
    return new CheckCarResult(null, maintenanceFrequency);
  }

  private MaintenanceScore mapToMaintenanceScore(MaintenanceFrequency maintenanceFrequency) {
    if (maintenanceFrequency == LOW || maintenanceFrequency == VERY_LOW) {
      return POOR;
    }
    if (maintenanceFrequency == MEDIUM) {
      return AVERAGE;
    }

    return GOOD;
  }
}
