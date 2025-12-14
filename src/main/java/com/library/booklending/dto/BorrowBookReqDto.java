package com.library.booklending.dto;

import jakarta.validation.constraints.NotNull;

public record BorrowBookReqDto(

    @NotNull(message = "{error.borrow.book.empty}")
    Long bookId,

    @NotNull(message = "{error.borrow.member.empty}")
    Long memberId
) {

}
