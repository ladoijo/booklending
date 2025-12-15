package com.library.booklending.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.library.booklending.dto.MemberReqDto;
import com.library.booklending.dto.MemberRespDto;
import com.library.booklending.service.MemberService;
import com.library.booklending.util.LanguageUtil;
import java.util.List;
import java.util.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;

class MemberControllerTest {

  private MemberService service;
  private MemberController controller;

  @BeforeEach
  void setUp() {
    service = Mockito.mock(MemberService.class);
    controller = new MemberController(service);

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
  void findAll_shouldReturn2xxWithData() {
    var data = List.of(Mockito.mock(MemberRespDto.class));
    Mockito.when(service.findAll()).thenReturn(data);

    var resp = controller.findAll();
    assertThat(resp.getStatusCode().is2xxSuccessful()).isTrue();
    assertThat(resp.getBody()).isNotNull();
    assertThat(extractData(resp.getBody())).isSameAs(data);

    Mockito.verify(service).findAll();
    Mockito.verifyNoMoreInteractions(service);
  }

  @Test
  void findById_shouldReturn2xxWithData() {
    long id = 10L;
    var member = Mockito.mock(MemberRespDto.class);
    Mockito.when(service.findById(id)).thenReturn(member);

    var resp = controller.findById(id);
    assertThat(resp.getStatusCode().is2xxSuccessful()).isTrue();
    assertThat(resp.getBody()).isNotNull();
    assertThat(extractData(resp.getBody())).isSameAs(member);

    Mockito.verify(service).findById(id);
    Mockito.verifyNoMoreInteractions(service);
  }

  @Test
  void save_shouldCallServiceSave_andReturn2xxWithData() {
    var req = Mockito.mock(MemberReqDto.class);
    var saved = Mockito.mock(MemberRespDto.class);
    Mockito.when(service.save(req)).thenReturn(saved);

    var resp = controller.save(req);
    assertThat(resp.getStatusCode().is2xxSuccessful()).isTrue();
    assertThat(resp.getBody()).isNotNull();
    assertThat(extractData(resp.getBody())).isSameAs(saved);

    Mockito.verify(service).save(req);
    Mockito.verifyNoMoreInteractions(service);
  }

  @Test
  void update_shouldCallServiceSave_andReturn2xxWithData() {
    var req = Mockito.mock(MemberReqDto.class);
    var updated = Mockito.mock(MemberRespDto.class);
    Mockito.when(service.save(req)).thenReturn(updated);

    var resp = controller.update(req);
    assertThat(resp.getStatusCode().is2xxSuccessful()).isTrue();
    assertThat(resp.getBody()).isNotNull();
    assertThat(extractData(resp.getBody())).isSameAs(updated);

    Mockito.verify(service).save(req);
    Mockito.verifyNoMoreInteractions(service);
  }

  @Test
  void deleteFlagById_shouldCallService_andReturn2xx() {
    var id = 7L;
    var resp = controller.deleteFlagById(id);

    assertThat(resp.getStatusCode().is2xxSuccessful()).isTrue();
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