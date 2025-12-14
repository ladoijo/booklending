package com.library.booklending.dto;

import com.library.booklending.entity.Book;

public record BookRespDto(
    long id,
    String title,
    String author,
    String isbn,
    int totalCopies,
    int availableCopies
) {

  public BookRespDto(Book book) {
    this(
        book.getId(),
        book.getTitle(),
        book.getAuthor(),
        book.getIsbn(),
        book.getTotalCopies(),
        book.getAvailableCopies()
    );
  }
}
