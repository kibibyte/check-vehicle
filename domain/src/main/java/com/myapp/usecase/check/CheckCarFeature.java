package com.myapp.usecase.check;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
enum CheckCarFeature {
  ACCIDENT_FREE("accident_free"),
  MAINTENANCE("maintenance");

  private final String value;
}
