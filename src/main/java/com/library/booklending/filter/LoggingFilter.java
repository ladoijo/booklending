package com.library.booklending.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Order(1)
@Component
@RequiredArgsConstructor
public class LoggingFilter extends OncePerRequestFilter {

  public static final String HEADER = "X-Correlation-Id";
  public static final String MDC_KEY = "correlationId";

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain) throws ServletException, IOException {
    try {
      MDC.put("requestId", UUID.randomUUID().toString());

      var correlationId = Optional.ofNullable(request.getHeader(HEADER))
          .filter(s -> !s.isBlank())
          .orElse(UUID.randomUUID().toString());

      MDC.put(MDC_KEY, correlationId);
      MDC.put("method", request.getMethod());
      MDC.put("uri", request.getRequestURI());
      MDC.put("queries", request.getQueryString());
      MDC.put("remoteAddr", request.getRemoteAddr());
      response.setHeader(HEADER, correlationId);
      chain.doFilter(request, response);
      MDC.put("status", String.valueOf(response.getStatus()));
    } finally {
      MDC.remove(MDC_KEY);
      MDC.clear();
    }
  }
}
