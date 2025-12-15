package com.library.booklending.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.library.booklending.util.LanguageUtil;
import java.util.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;

class LoanRuleExceptionTest {

  @BeforeEach
  void setUp() {
    var ms = Mockito.mock(MessageSource.class);
    Mockito.when(
            ms.getMessage(Mockito.anyString(), Mockito.<Object[]>any(), Mockito.any(Locale.class)))
        .thenAnswer(inv -> "translated:" + inv.getArgument(0));

    try {
      var f = LanguageUtil.class.getDeclaredField("messageSource");
      f.setAccessible(true);
      f.set(null, ms);
    } catch (Exception e) {
      throw new RuntimeException("Failed to set LanguageUtil.messageSource for tests", e);
    }
  }

  @Test
  void constructor_shouldUseLanguageUtilMessageAsExceptionMessage() {
    var ex = new LoanRuleException("error.loan.rule");
    assertThat(ex.getMessage()).isEqualTo("translated:error.loan.rule");
  }
}