package com.myapp.usecase.check;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.Set;

import com.myapp.exceptions.InvalidArgumentException;

import lombok.Value;

@Value
class CheckCarQuery {

  String vin;
  Set<CheckCarFeature> featuresToCheck;

  CheckCarQuery(String vin, Set<CheckCarFeature> featuresToCheck) {
    if (isBlank(vin)) {
      throw new InvalidArgumentException("INVALID_VIN", "Vin cannot be empty");
    }
    if (featuresToCheck == null || featuresToCheck.isEmpty()) {
      throw new InvalidArgumentException("EMPTY_FEATURES", "Features cannot be empty");
    }

    this.vin = vin;
    this.featuresToCheck = featuresToCheck;
  }
}
