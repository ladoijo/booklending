package com.library.booklending.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.library.booklending.dto.BookReqDto;
import com.library.booklending.dto.BookRespDto;
import com.library.booklending.service.BookService;
import com.library.booklending.util.LanguageUtil;
import java.util.List;
import java.util.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;

class BookControllerTest {

  private BookService service;
  private BookController controller;

  @BeforeEach
  void setUp() {
    service = Mockito.mock(BookService.class);
    controller = new BookController(service);

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
  void findAll_shouldReturnOkWithData() {
    var data = List.of(Mockito.mock(BookRespDto.class));
    Mockito.when(service.findAll()).thenReturn(data);

    var resp = controller.findAll();

    assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(resp.getBody()).isNotNull();
    assertThat(extractData(resp.getBody())).isSameAs(data);

    Mockito.verify(service).findAll();
    Mockito.verifyNoMoreInteractions(service);
  }

  @Test
  void findById_shouldReturnOkWithData() {
    var id = 10L;
    var book = Mockito.mock(BookRespDto.class);
    Mockito.when(service.findById(id)).thenReturn(book);

    var resp = controller.findById(id);

    assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(resp.getBody()).isNotNull();
    assertThat(extractData(resp.getBody())).isSameAs(book);

    Mockito.verify(service).findById(id);
    Mockito.verifyNoMoreInteractions(service);
  }

  @Test
  void save_shouldCallServiceSave_andReturnOkWithData() {
    var req = new BookReqDto(
        null,
        "Clean Code",
        "Robert C. Martin",
        "9780132350884",
        3,
        3
    );

    var saved = Mockito.mock(BookRespDto.class);
    Mockito.when(service.save(req)).thenReturn(saved);

    var resp = controller.save(req);

    assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(resp.getBody()).isNotNull();
    assertThat(extractData(resp.getBody())).isSameAs(saved);

    Mockito.verify(service).save(req);
    Mockito.verifyNoMoreInteractions(service);
  }

  @Test
  void update_shouldCallServiceSave_andReturnOkWithData() {
    var req = new BookReqDto(
        99L,
        "Refactoring",
        "Martin Fowler",
        "9780201485677",
        5,
        2
    );

    var updated = Mockito.mock(BookRespDto.class);
    Mockito.when(service.save(req)).thenReturn(updated);

    var resp = controller.update(req);

    assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(resp.getBody()).isNotNull();
    assertThat(extractData(resp.getBody())).isSameAs(updated);

    Mockito.verify(service).save(req);
    Mockito.verifyNoMoreInteractions(service);
  }

  @Test
  void deleteFlagById_shouldCallService_andReturnOk() {
    long id = 7L;

    var resp = controller.deleteFlagById(id);

    assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(resp.getBody()).isNotNull();

    Mockito.verify(service).deleteFlagById(id);
    Mockito.verifyNoMoreInteractions(service);
  }

  private static Object extractData(Object apiRespDto) {
    if (apiRespDto == null) {
      return null;
    }

    try {
      var m = apiRespDto.getClass().getMethod("data");
      return m.invoke(apiRespDto);
    } catch (Exception ignored) {
    }

    try {
      var m = apiRespDto.getClass().getMethod("getData");
      return m.invoke(apiRespDto);
    } catch (Exception ignored) {
    }

    return null;
  }
}