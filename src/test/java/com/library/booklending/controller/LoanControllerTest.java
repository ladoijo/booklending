package com.library.booklending.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.library.booklending.dto.BorrowBookReqDto;
import com.library.booklending.dto.BorrowBookRespDto;
import com.library.booklending.usecase.LoanUseCase;
import com.library.booklending.util.LanguageUtil;
import java.util.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;

class LoanControllerTest {

  private LoanUseCase useCase;
  private LoanController controller;

  @BeforeEach
  void setUp() {
    useCase = Mockito.mock(LoanUseCase.class);
    controller = new LoanController(useCase);
    
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
  void borrowBook_shouldCallUseCase_andReturn2xxWithData() {
    var req = Mockito.mock(BorrowBookReqDto.class);
    var data = Mockito.mock(BorrowBookRespDto.class);
    Mockito.when(useCase.borrowBook(req)).thenReturn(data);

    var resp = controller.borrowBook(req);

    assertThat(resp.getStatusCode().is2xxSuccessful()).isTrue();
    assertThat(resp.getBody()).isNotNull();
    assertThat(extractData(resp.getBody())).isSameAs(data);

    Mockito.verify(useCase).borrowBook(req);
    Mockito.verifyNoMoreInteractions(useCase);
  }

  @Test
  void returnBook_shouldCallUseCase_andReturn2xxWithData() {
    var id = 123L;
    var data = Mockito.mock(BorrowBookRespDto.class);
    Mockito.when(useCase.returnBook(id)).thenReturn(data);

    var resp = controller.returnBook(id);

    assertThat(resp.getStatusCode().is2xxSuccessful()).isTrue();
    assertThat(resp.getBody()).isNotNull();
    assertThat(extractData(resp.getBody())).isSameAs(data);

    Mockito.verify(useCase).returnBook(id);
    Mockito.verifyNoMoreInteractions(useCase);
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