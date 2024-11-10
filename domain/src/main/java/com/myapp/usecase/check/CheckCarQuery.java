package com.myapp.usecase.check;

import static com.myapp.usecase.check.CheckCarFeature.ACCIDENT_FREE;
import static com.myapp.usecase.check.CheckCarFeature.MAINTENANCE;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.HashSet;
import java.util.List;

import com.myapp.exceptions.InvalidArgumentException;

import lombok.Value;

@Value
class CheckCarQuery {

  String vin;
  List<CheckCarFeature> featuresToCheck;

  CheckCarQuery(String vin, List<CheckCarFeature> featuresToCheck) {
    if (isBlank(vin)) {
      throw new InvalidArgumentException("INVALID_VIN", "Vin cannot be empty");
    }
    if (featuresToCheck == null || featuresToCheck.isEmpty()) {
      throw new InvalidArgumentException("EMPTY_FEATURES", "Features cannot be empty");
    }

    this.vin = vin;
    this.featuresToCheck = featuresToCheck;
  }

  boolean isCheckAll() {
    return new HashSet<>(featuresToCheck).containsAll(List.of(ACCIDENT_FREE, MAINTENANCE));
  }

  boolean isCheckFeature(CheckCarFeature feature) {
    return featuresToCheck.contains(feature);
  }
}
