package com.library.booklending.dto;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record ApiRespDto<T>(
        int code,
        String status,
        String message,
        Instant timestamp,
        T data,
        Map<String, List<String>> errors) {
    public ApiRespDto(int code, String status, String message, T data, Map<String, List<String>> errors) {
        this(code, status, message, Instant.now(), data, errors);
    }
}
