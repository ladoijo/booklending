package com.library.booklending.dto;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record ApiErrorRespDto(
    int code,
    String status,
    String message,
    Instant timestamp,
    Map<String, List<String>> errors) {

  public ApiErrorRespDto(int code, String status, String message,
      Map<String, List<String>> errors) {
    this(code, status, message, Instant.now(), errors);
  }
}
