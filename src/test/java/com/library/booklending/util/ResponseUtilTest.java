package com.library.booklending.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BeanPropertyBindingResult;

class ResponseUtilTest {

  @BeforeEach
  void setUp() {
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
  void ok_shouldReturn200AndSuccessMessage() {
    var resp = ResponseUtil.ok();

    assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(resp.getBody()).isNotNull();

    assertThat(read(resp.getBody(), "message", "getMessage")).isEqualTo("response.success");
    assertThat(read(resp.getBody(), "data", "getData")).isNull();
  }

  @Test
  void okWithData_shouldReturn200AndData() {
    var data = "HELLO";

    var resp = ResponseUtil.okWithData(data);

    assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(resp.getBody()).isNotNull();

    assertThat(read(resp.getBody(), "message", "getMessage")).isEqualTo("response.success");
    assertThat(read(resp.getBody(), "data", "getData")).isEqualTo(data);
  }

  @Test
  void failWithMessage_shouldReturnGivenStatusAndMessage() {
    var resp =
        ResponseUtil.failWithMessage(HttpStatus.BAD_REQUEST, "Invalid request");

    assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(resp.getBody()).isNotNull();

    assertThat(read(resp.getBody(), "message", "getMessage")).isEqualTo("Invalid request");
    assertThat(read(resp.getBody(), "errors", "getErrors")).isNull();
  }

  @Test
  void failWithErrors_shouldUseDefaultMessageIfPresent_otherwiseTranslateFromCode() {
    var target = new Form();
    var errors = new BeanPropertyBindingResult(target, "form");
    errors.rejectValue("title", "error.book.title.empty", "");
    errors.rejectValue("author", "error.book.author.empty", "Author must not be blank");

    var resp = ResponseUtil.failWithErrors(HttpStatus.BAD_REQUEST, "Validation failed", errors);

    assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(resp.getBody()).isNotNull();
    assertThat(read(resp.getBody(), "message", "getMessage")).isEqualTo("Validation failed");

    @SuppressWarnings("unchecked")
    Map<String, List<String>> errMap =
        (Map<String, List<String>>) read(resp.getBody(), "errors", "getErrors");

    assertThat(errMap).isNotNull();
    assertThat(errMap.get("title")).containsExactly("error.book.title.empty");
    assertThat(errMap.get("author")).containsExactly("Author must not be blank");
  }

  private static Object read(Object obj, String... methodNames) {
    for (var name : methodNames) {
      try {
        var m = obj.getClass().getMethod(name);
        return m.invoke(obj);
      } catch (Exception ignored) {
      }
    }
    return null;
  }
  
  private static class Form {

    private String title;
    private String author;

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public String getAuthor() {
      return author;
    }

    public void setAuthor(String author) {
      this.author = author;
    }
  }
}