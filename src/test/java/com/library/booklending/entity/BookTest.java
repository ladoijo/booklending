package com.library.booklending.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.library.booklending.dto.BookReqDto;
import org.junit.jupiter.api.Test;

class BookTest {

  @Test
  void constructor_fromReqDto_whenIdProvided_shouldCopyIdAndFields() {
    var dto =
        new BookReqDto(
            10L,
            "Clean Code",
            "Robert C. Martin",
            "9780132350884",
            5,
            3
        );

    var book = new Book(dto);
    assertEquals(10L, book.getId());
    assertEquals("Clean Code", book.getTitle());
    assertEquals("Robert C. Martin", book.getAuthor());
    assertEquals("9780132350884", book.getIsbn());
    assertEquals(5, book.getTotalCopies());
    assertEquals(3, book.getAvailableCopies());
  }

  @Test
  void constructor_fromReqDto_whenIdNull_shouldCopyFieldsAndKeepIdNull() {
    var dto =
        new BookReqDto(
            null,
            "Domain-Driven Design",
            "Eric Evans",
            "9780321125217",
            7,
            7
        );

    var book = new Book(dto);
    assertEquals(0, book.getId());
    assertEquals("Domain-Driven Design", book.getTitle());
    assertEquals("Eric Evans", book.getAuthor());
    assertEquals("9780321125217", book.getIsbn());
    assertEquals(7, book.getTotalCopies());
    assertEquals(7, book.getAvailableCopies());
  }

  @Test
  void constructor_fromReqDto_whenTotalCopiesNull_shouldThrowNullPointerException() {
    var dto =
        new BookReqDto(
            null,
            "Some Title",
            "Some Author",
            "9780132350884",
            null,
            0
        );

    assertThrows(NullPointerException.class, () -> new Book(dto));
  }

  @Test
  void constructor_fromReqDto_whenAvailableCopiesNull_shouldThrowNullPointerException() {
    var dto =
        new BookReqDto(
            null,
            "Some Title",
            "Some Author",
            "9780132350884",
            1,
            null
        );

    assertThrows(NullPointerException.class, () -> new Book(dto));
  }

  @Test
  void constructor_withId_shouldSetId() {
    var book = new Book(123L);
    assertEquals(123L, book.getId());
  }
}
