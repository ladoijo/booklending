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

import com.library.booklending.dto.BookReqDto;
import com.library.booklending.entity.Book;
import com.library.booklending.repository.BookRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

  @Mock
  private BookRepository repository;

  private BookService service;

  @BeforeEach
  void setUp() {
    service = new BookService(repository);
  }

  @Test
  void findAll_shouldMapEntitiesToDtos() {
    var b1 = mock(Book.class);
    var b2 = mock(Book.class);
    when(repository.findAll()).thenReturn(List.of(b1, b2));

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
    var book = mock(Book.class);
    when(repository.findById(id)).thenReturn(Optional.of(book));

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
    var reqDto = mock(BookReqDto.class);
    var saved = mock(Book.class);
    when(repository.save(any(Book.class))).thenReturn(saved);

    var result = service.save(reqDto);
    assertNotNull(result);

    var captor = ArgumentCaptor.forClass(Book.class);
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

  @Test
  void decrementAvailableCopies_shouldDelegateAndReturnValue() {
    var id = 5L;
    when(repository.decrementAvailableCopies(id)).thenReturn(1);

    int updated = service.decrementAvailableCopies(id);
    assertEquals(1, updated);
    verify(repository).decrementAvailableCopies(id);
    verifyNoMoreInteractions(repository);
  }

  @Test
  void incrementAvailableCopies_shouldDelegateAndReturnValue() {
    var id = 5L;
    when(repository.incrementAvailableCopies(id)).thenReturn(1);

    var updated = service.incrementAvailableCopies(id);
    assertEquals(1, updated);
    verify(repository).incrementAvailableCopies(id);
    verifyNoMoreInteractions(repository);
  }
}
