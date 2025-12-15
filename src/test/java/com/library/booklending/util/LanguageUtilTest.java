package com.library.booklending.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.eq;

import java.util.List;
import java.util.Locale;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

class LanguageUtilTest {

  @AfterEach
  void tearDown() throws Exception {
    LocaleContextHolder.resetLocaleContext();

    var f = LanguageUtil.class.getDeclaredField("messageSource");
    f.setAccessible(true);
    f.set(null, null);
  }

  @Test
  void getMessage_shouldCallMessageSourceWithCurrentLocale_andArgsArray() {
    var ms = Mockito.mock(MessageSource.class);

    var locale = Locale.ENGLISH;
    LocaleContextHolder.setLocale(locale);

    new LanguageUtil(ms);

    Mockito.when(ms.getMessage(eq("hello"), Mockito.<Object[]>any(), eq(locale)))
        .thenReturn("Hello!");

    var result = LanguageUtil.getMessage("hello");

    assertThat(result).isEqualTo("Hello!");

    var argsCaptor = ArgumentCaptor.forClass(Object[].class);
    Mockito.verify(ms).getMessage(eq("hello"), argsCaptor.capture(), eq(locale));

    var args = argsCaptor.getValue();
    assertThat(args).hasSize(1);
    assertThat(args[0]).isInstanceOf(List.class);
    assertThat((List<?>) args[0]).isEmpty();

    Mockito.verifyNoMoreInteractions(ms);
  }
}