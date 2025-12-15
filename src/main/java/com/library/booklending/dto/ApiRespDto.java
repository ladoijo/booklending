package com.library.booklending.dto;

import java.time.Instant;

public record ApiRespDto<T>(
    int code,
    String status,
    String message,
    Instant timestamp,
    T data) {

  public ApiRespDto(int code, String status, String message, T data) {
    this(code, status, message, Instant.now(), data);
  }
}
