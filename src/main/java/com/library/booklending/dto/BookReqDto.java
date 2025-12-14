package com.library.booklending.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.ISBN;

public record BookReqDto(
    Long id,

    @NotBlank(message = "{error.book.title.empty}")
    String title,

    @NotBlank(message = "{error.book.author.empty}")
    String author,

    @NotBlank(message = "{error.book.isbn.empty}")
    @ISBN(type = ISBN.Type.ANY, message = "{error.book.isbn.invalid}")
    String isbn,

    @NotNull(message = "{error.book.totalCopies.empty}")
    @Min(value = 1, message = "{error.book.totalCopies.min}")
    Integer totalCopies,

    @Min(value = 0, message = "{error.book.availableCopies.min}")
    Integer availableCopies) {

}
