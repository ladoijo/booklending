package com.library.booklending.usecase;

import com.library.booklending.dto.BorrowBookReqDto;
import com.library.booklending.dto.BorrowBookRespDto;
import com.library.booklending.entity.Loan;
import com.library.booklending.exception.LoanRuleException;
import com.library.booklending.exception.ResourceNotFoundException;
import com.library.booklending.service.BookService;
import com.library.booklending.service.LoanService;
import com.library.booklending.service.MemberService;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class LoanUseCase {

  private final LoanService service;
  private final BookService bookService;
  private final MemberService memberService;

  @Value("${library.rules.borrow.max-duration-days}")
  private int maxDurationDays;

  @Transactional
  public BorrowBookRespDto borrowBook(BorrowBookReqDto reqDto) {
    var isMemberExists = memberService.existsById(reqDto.memberId());
    if (!isMemberExists) {
      throw new ResourceNotFoundException("error.member.not-found");
    }

    var isBookExists = bookService.existsById(reqDto.bookId());
    if (!isBookExists) {
      throw new ResourceNotFoundException("error.book.not-found");
    }

    var isLimitActiveLoansReached = service.isLimitActiveLoansReached(reqDto.memberId());
    if (isLimitActiveLoansReached) {
      throw new LoanRuleException("error.borrow.limit-active-loans");
    }

    var hasOverdueActiveLoans = service.hasOverdueActiveLoans(reqDto.memberId());
    if (hasOverdueActiveLoans) {
      throw new LoanRuleException("error.borrow.overdue-active-loans");
    }

    var updateBookCopies = bookService.decrementAvailableCopies(reqDto.bookId());
    if (updateBookCopies == 0) {
      throw new LoanRuleException("error.book.not-available");
    }

    var loan = new Loan(reqDto);
    loan.setDueDate(loan.getBorrowedAt().toLocalDate().plusDays(maxDurationDays));
    loan = service.save(loan);
    return new BorrowBookRespDto(loan);
  }

  @Transactional
  public BorrowBookRespDto returnBook(Long id) {
    var loan = service.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("error.loan.not-found"));

    if (loan.getReturnedAt() != null) {
      throw new LoanRuleException("error.loan.book-returned");
    }

    loan.setReturnedAt(OffsetDateTime.now());
    loan = service.save(loan);
    var bookId = loan.getBook().getId();
    var updated = bookService.incrementAvailableCopies(bookId);
    if (updated == 0) {
      throw new LoanRuleException("error.book.unable-increment");
    }

    return new BorrowBookRespDto(loan);
  }
}
