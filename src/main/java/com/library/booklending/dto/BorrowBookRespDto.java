package com.library.booklending.dto;

import com.library.booklending.entity.Loan;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public record BorrowBookRespDto(
    long id,
    long bookId,
    long memberId,
    OffsetDateTime borrowedAt,
    LocalDate dueDate,
    OffsetDateTime returnedAt
) {

  public BorrowBookRespDto(Loan loan) {
    this(
        loan.getId(),
        loan.getBook().getId(),
        loan.getMember().getId(),
        loan.getBorrowedAt(),
        loan.getDueDate(),
        loan.getReturnedAt()
    );
  }
}
