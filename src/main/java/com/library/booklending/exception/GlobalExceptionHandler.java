package com.library.booklending.exception;

import com.library.booklending.dto.ApiErrorRespDto;
import com.library.booklending.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ApiErrorRespDto> handleValidationErrors(BindException e) {
    return ResponseUtil.failWithErrors(HttpStatus.BAD_REQUEST, "Validation failed",
        e.getBindingResult());
  }

  @ExceptionHandler(LoanRuleException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ResponseEntity<ApiErrorRespDto> handleLoanRuleException(LoanRuleException e) {
    return ResponseUtil.failWithMessage(HttpStatus.CONFLICT, e.getMessage());
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<ApiErrorRespDto> handleNotFound(ResourceNotFoundException e) {
    return ResponseUtil.failWithMessage(HttpStatus.NOT_FOUND, e.getMessage());
  }

  @ExceptionHandler(NullPointerException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<?> handleNPE(NullPointerException ex) {
    return ResponseUtil.failWithMessage(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<?> handleGeneric(Exception ex) {
    return ResponseUtil.failWithMessage(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
  }
}
