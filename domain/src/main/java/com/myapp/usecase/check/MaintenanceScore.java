package com.myapp.usecase.check;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
enum MaintenanceScore {
  POOR("poor"),
  AVERAGE("average"),
  GOOD("good");

  private final String value;
}

