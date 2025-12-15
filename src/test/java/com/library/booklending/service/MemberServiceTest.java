package com.library.booklending.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.library.booklending.dto.MemberReqDto;
import com.library.booklending.entity.Member;
import com.library.booklending.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

  @Mock
  private MemberRepository repository;

  @InjectMocks
  private MemberService service;

  @Test
  void findAll_shouldMapEntitiesToDtos() {
    var m1 = mock(Member.class);
    var m2 = mock(Member.class);
    when(repository.findAll()).thenReturn(List.of(m1, m2));

    var result = service.findAll();
    assertNotNull(result);
    assertEquals(2, result.size());
    assertNotNull(result.get(0));
    assertNotNull(result.get(1));
    verify(repository).findAll();
    verifyNoMoreInteractions(repository);
  }

  @Test
  void findById_whenFound_shouldReturnDto() {
    var id = 10L;
    var member = mock(Member.class);
    when(repository.findById(id)).thenReturn(Optional.of(member));

    var result = service.findById(id);
    assertNotNull(result);
    verify(repository).findById(id);
    verifyNoMoreInteractions(repository);
  }

  @Test
  void findById_whenNotFound_shouldReturnNull() {
    var id = 99L;
    when(repository.findById(id)).thenReturn(Optional.empty());

    var result = service.findById(id);
    assertNull(result);
    verify(repository).findById(id);
    verifyNoMoreInteractions(repository);
  }

  @Test
  void existsById_shouldDelegateToRepository() {
    var id = 1L;
    when(repository.existsById(id)).thenReturn(true);

    var exists = service.existsById(id);
    assertTrue(exists);
    verify(repository).existsById(id);
    verifyNoMoreInteractions(repository);
  }

  @Test
  void save_shouldPersistAndReturnDto() {
    var reqDto = mock(MemberReqDto.class);
    var saved = mock(Member.class);
    when(repository.save(any(Member.class))).thenReturn(saved);

    var result = service.save(reqDto);
    assertNotNull(result);

    var captor = ArgumentCaptor.forClass(Member.class);
    verify(repository).save(captor.capture());
    assertNotNull(captor.getValue());
    verifyNoMoreInteractions(repository);
  }

  @Test
  void deleteFlagById_shouldCallUpdateDeleteFlagByIdWithTrue() {
    var id = 7L;
    service.deleteFlagById(id);

    verify(repository).updateDeleteFlagById(id, true);
    verifyNoMoreInteractions(repository);
  }
}
