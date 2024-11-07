package com.myapp.usecase.check;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
enum MaintenanceFrequency {
  VERY_LOW("very-low"),
  LOW("low"),
  MEDIUM("medium"),
  HIGH("high");

  private final String value;
}

