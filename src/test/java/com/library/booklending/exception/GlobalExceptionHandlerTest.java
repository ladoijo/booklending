package com.library.booklending.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.library.booklending.util.LanguageUtil;
import java.util.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;

class GlobalExceptionHandlerTest {

  private GlobalExceptionHandler handler;

  @BeforeEach
  void setUp() {
    handler = new GlobalExceptionHandler();
    var ms = Mockito.mock(MessageSource.class);
    Mockito.when(
            ms.getMessage(Mockito.anyString(), Mockito.<Object[]>any(), Mockito.any(Locale.class)))
        .thenAnswer(inv -> inv.getArgument(0));

    try {
      var f = LanguageUtil.class.getDeclaredField("messageSource");
      f.setAccessible(true);
      f.set(null, ms);
    } catch (Exception e) {
      throw new RuntimeException("Failed to set LanguageUtil.messageSource for tests", e);
    }
  }

  @Test
  void handleValidationErrors_shouldReturnBadRequest() {
    var target = new TitleForm();
    var ex = new BindException(target, "target");
    ex.getBindingResult().rejectValue("title", "error.book.title.empty", "Title must not be blank");

    var resp = handler.handleValidationErrors(ex);
    assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(resp.getBody()).isNotNull();
    assertThat(extractMessage(resp.getBody())).isNotBlank();
  }

  @Test
  void handleLoanRuleException_shouldReturnConflict() {
    var ex = new LoanRuleException("Loan rule violated");
    var resp = handler.handleLoanRuleException(ex);

    assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    assertThat(resp.getBody()).isNotNull();
    assertThat(extractMessage(resp.getBody())).contains("Loan rule violated");
  }

  @Test
  void handleNotFound_shouldReturnNotFound() {
    var ex = new ResourceNotFoundException("Book not found");
    var resp = handler.handleNotFound(ex);

    assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(resp.getBody()).isNotNull();
    assertThat(extractMessage(resp.getBody())).contains("Book not found");
  }

  @Test
  void handleNPE_shouldReturnInternalServerError() {
    var ex = new NullPointerException("NPE happened");
    var resp = handler.handleNPE(ex);

    assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    assertThat(resp.getBody()).isNotNull();
    assertThat(extractMessage(resp.getBody())).contains("NPE happened");
  }

  @Test
  void handleGeneric_shouldReturnInternalServerError() {
    var ex = new Exception("Boom");
    var resp = handler.handleGeneric(ex);

    assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    assertThat(resp.getBody()).isNotNull();
    assertThat(extractMessage(resp.getBody())).contains("Boom");
  }

  private static class TitleForm {

    private String title;

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }
  }

  private static String extractMessage(Object body) {
    if (body == null) {
      return null;
    }

    try {
      Object v = body.getClass().getMethod("message").invoke(body);
      return v == null ? null : v.toString();
    } catch (Exception ignored) {
      // ignore
    }

    try {
      Object v = body.getClass().getMethod("getMessage").invoke(body);
      return v == null ? null : v.toString();
    } catch (Exception ignored) {
      // ignore
    }

    return body.toString();
  }
}