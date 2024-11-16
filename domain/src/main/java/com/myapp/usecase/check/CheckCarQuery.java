package com.myapp.usecase.check;

import static org.apache.commons.lang3.StringUtils.isBlank;

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

  boolean isCheckFeature(CheckCarFeature feature) {
    return featuresToCheck.contains(feature);
  }
}
