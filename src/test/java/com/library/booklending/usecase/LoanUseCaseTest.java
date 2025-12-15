package com.library.booklending.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.library.booklending.dto.BorrowBookReqDto;
import com.library.booklending.entity.Book;
import com.library.booklending.entity.Loan;
import com.library.booklending.entity.Member;
import com.library.booklending.exception.LoanRuleException;
import com.library.booklending.exception.ResourceNotFoundException;
import com.library.booklending.service.BookService;
import com.library.booklending.service.LoanService;
import com.library.booklending.service.MemberService;
import com.library.booklending.util.LanguageUtil;
import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.test.util.ReflectionTestUtils;

class LoanUseCaseTest {

  private LoanService service;
  private BookService bookService;
  private MemberService memberService;
  private LoanUseCase useCase;

  @BeforeEach
  void setUp() {
    service = Mockito.mock(LoanService.class);
    bookService = Mockito.mock(BookService.class);
    memberService = Mockito.mock(MemberService.class);
    useCase = new LoanUseCase(service, bookService, memberService);

    ReflectionTestUtils.setField(useCase, "maxDurationDays", 7);
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

  // =========================
  // borrowBook()
  // =========================

  @Test
  void borrowBook_whenMemberNotExists_shouldThrowResourceNotFound() {
    var req = Mockito.mock(BorrowBookReqDto.class);
    Mockito.when(req.memberId()).thenReturn(1L);

    Mockito.when(memberService.existsById(1L)).thenReturn(false);

    assertThatThrownBy(() -> useCase.borrowBook(req))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage("error.member.not-found");

    Mockito.verify(memberService).existsById(1L);
    Mockito.verifyNoInteractions(bookService);
    Mockito.verifyNoInteractions(service);
  }

  @Test
  void borrowBook_whenBookNotExists_shouldThrowResourceNotFound() {
    var req = Mockito.mock(BorrowBookReqDto.class);
    Mockito.when(req.memberId()).thenReturn(1L);
    Mockito.when(req.bookId()).thenReturn(2L);

    Mockito.when(memberService.existsById(1L)).thenReturn(true);
    Mockito.when(bookService.existsById(2L)).thenReturn(false);

    assertThatThrownBy(() -> useCase.borrowBook(req))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage("error.book.not-found");

    Mockito.verify(memberService).existsById(1L);
    Mockito.verify(bookService).existsById(2L);
    Mockito.verifyNoInteractions(service);
  }

  @Test
  void borrowBook_whenLimitActiveLoansReached_shouldThrowLoanRule() {
    var req = Mockito.mock(BorrowBookReqDto.class);
    Mockito.when(req.memberId()).thenReturn(1L);
    Mockito.when(req.bookId()).thenReturn(2L);

    Mockito.when(memberService.existsById(1L)).thenReturn(true);
    Mockito.when(bookService.existsById(2L)).thenReturn(true);
    Mockito.when(service.isLimitActiveLoansReached(1L)).thenReturn(true);

    assertThatThrownBy(() -> useCase.borrowBook(req))
        .isInstanceOf(LoanRuleException.class)
        .hasMessage("error.borrow.limit-active-loans");

    Mockito.verify(service).isLimitActiveLoansReached(1L);
    Mockito.verify(service, Mockito.never()).hasOverdueActiveLoans(Mockito.anyLong());
    Mockito.verify(bookService, Mockito.never()).decrementAvailableCopies(Mockito.anyLong());
    Mockito.verify(service, Mockito.never()).save(Mockito.any());
  }

  @Test
  void borrowBook_whenHasOverdueActiveLoans_shouldThrowLoanRule() {
    var req = Mockito.mock(BorrowBookReqDto.class);
    Mockito.when(req.memberId()).thenReturn(1L);
    Mockito.when(req.bookId()).thenReturn(2L);

    Mockito.when(memberService.existsById(1L)).thenReturn(true);
    Mockito.when(bookService.existsById(2L)).thenReturn(true);
    Mockito.when(service.isLimitActiveLoansReached(1L)).thenReturn(false);
    Mockito.when(service.hasOverdueActiveLoans(1L)).thenReturn(true);

    assertThatThrownBy(() -> useCase.borrowBook(req))
        .isInstanceOf(LoanRuleException.class)
        .hasMessage("error.borrow.overdue-active-loans");

    Mockito.verify(service).hasOverdueActiveLoans(1L);
    Mockito.verify(bookService, Mockito.never()).decrementAvailableCopies(Mockito.anyLong());
    Mockito.verify(service, Mockito.never()).save(Mockito.any());
  }

  @Test
  void borrowBook_whenBookNotAvailable_shouldThrowLoanRule() {
    var req = Mockito.mock(BorrowBookReqDto.class);
    Mockito.when(req.memberId()).thenReturn(1L);
    Mockito.when(req.bookId()).thenReturn(2L);

    Mockito.when(memberService.existsById(1L)).thenReturn(true);
    Mockito.when(bookService.existsById(2L)).thenReturn(true);
    Mockito.when(service.isLimitActiveLoansReached(1L)).thenReturn(false);
    Mockito.when(service.hasOverdueActiveLoans(1L)).thenReturn(false);
    Mockito.when(bookService.decrementAvailableCopies(2L)).thenReturn(0);

    assertThatThrownBy(() -> useCase.borrowBook(req))
        .isInstanceOf(LoanRuleException.class)
        .hasMessage("error.book.not-available");

    Mockito.verify(bookService).decrementAvailableCopies(2L);
    Mockito.verify(service, Mockito.never()).save(Mockito.any());
  }

  @Test
  void borrowBook_success_shouldSetDueDateAndSave() {
    var req = Mockito.mock(BorrowBookReqDto.class);
    Mockito.when(req.memberId()).thenReturn(1L);
    Mockito.when(req.bookId()).thenReturn(2L);

    Mockito.when(memberService.existsById(1L)).thenReturn(true);
    Mockito.when(bookService.existsById(2L)).thenReturn(true);
    Mockito.when(service.isLimitActiveLoansReached(1L)).thenReturn(false);
    Mockito.when(service.hasOverdueActiveLoans(1L)).thenReturn(false);
    Mockito.when(bookService.decrementAvailableCopies(2L)).thenReturn(1);

    Mockito.when(service.save(Mockito.any(Loan.class)))
        .thenAnswer(inv -> inv.getArgument(0));

    var resp = useCase.borrowBook(req);

    assertThat(resp).isNotNull();

    var loanCaptor = ArgumentCaptor.forClass(Loan.class);
    Mockito.verify(service).save(loanCaptor.capture());

    var savedLoan = loanCaptor.getValue();
    assertThat(savedLoan).isNotNull();
    assertThat(savedLoan.getBorrowedAt()).isNotNull();
    assertThat(savedLoan.getDueDate())
        .isEqualTo(savedLoan.getBorrowedAt().toLocalDate().plusDays(7));

    Mockito.verify(memberService).existsById(1L);
    Mockito.verify(bookService).existsById(2L);
    Mockito.verify(service).isLimitActiveLoansReached(1L);
    Mockito.verify(service).hasOverdueActiveLoans(1L);
    Mockito.verify(bookService).decrementAvailableCopies(2L);
  }

  // =========================
  // returnBook()
  // =========================

  @Test
  void returnBook_whenNotFound_shouldThrowResourceNotFound() {
    Mockito.when(service.findById(10L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> useCase.returnBook(10L))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage("error.loan.not-found");

    Mockito.verify(service).findById(10L);
    Mockito.verifyNoInteractions(bookService);
  }

  @Test
  void returnBook_whenAlreadyReturned_shouldThrowLoanRule() {
    var loan = Mockito.mock(Loan.class);
    Mockito.when(service.findById(10L)).thenReturn(Optional.of(loan));
    Mockito.when(loan.getReturnedAt()).thenReturn(OffsetDateTime.now());

    assertThatThrownBy(() -> useCase.returnBook(10L))
        .isInstanceOf(LoanRuleException.class)
        .hasMessage("error.loan.book-returned");

    Mockito.verify(loan, Mockito.never()).setReturnedAt(Mockito.any());
    Mockito.verify(service, Mockito.never()).save(Mockito.any());
    Mockito.verify(bookService, Mockito.never()).incrementAvailableCopies(Mockito.anyLong());
  }

  @Test
  void returnBook_whenIncrementFailed_shouldThrowLoanRule() {
    var loanId = 10L;
    var bookId = 2L;
    var memberId = 1L;
    var member = Mockito.mock(Member.class);
    Mockito.when(member.getId()).thenReturn(memberId);

    var book = Mockito.mock(Book.class);
    Mockito.when(book.getId()).thenReturn(bookId);

    var returnedAtRef = new AtomicReference<>(null);
    var loan = Mockito.mock(Loan.class);
    Mockito.when(loan.getReturnedAt()).thenAnswer(inv -> returnedAtRef.get());
    Mockito.when(loan.getBook()).thenReturn(book);
    Mockito.when(loan.getMember()).thenReturn(member);
    Mockito.doAnswer(inv -> {
          returnedAtRef.set(inv.getArgument(0));
          return null;
        })
        .when(loan).setReturnedAt(Mockito.any(OffsetDateTime.class));

    Mockito.when(service.findById(loanId)).thenReturn(Optional.of(loan));
    Mockito.when(service.save(loan)).thenReturn(loan);
    Mockito.when(bookService.incrementAvailableCopies(bookId)).thenReturn(0);

    assertThatThrownBy(() -> useCase.returnBook(loanId))
        .isInstanceOf(LoanRuleException.class)
        .hasMessage("error.book.unable-increment");

    Mockito.verify(loan).setReturnedAt(Mockito.any(OffsetDateTime.class));
    Mockito.verify(service).save(loan);
    Mockito.verify(bookService).incrementAvailableCopies(bookId);
  }

  @Test
  void returnBook_success_shouldSetReturnedAt_SaveAndIncrement() {
    var loanId = 10L;
    var bookId = 2L;
    var memberId = 1L;
    var member = Mockito.mock(Member.class);
    Mockito.when(member.getId()).thenReturn(memberId);

    var book = Mockito.mock(Book.class);
    Mockito.when(book.getId()).thenReturn(bookId);

    var returnedAtRef = new AtomicReference<>(null);
    var loan = Mockito.mock(Loan.class);
    Mockito.when(loan.getReturnedAt()).thenAnswer(inv -> returnedAtRef.get());
    Mockito.when(loan.getBook()).thenReturn(book);
    Mockito.when(loan.getMember()).thenReturn(member);
    Mockito.doAnswer(inv -> {
          returnedAtRef.set(inv.getArgument(0));
          return null;
        })
        .when(loan).setReturnedAt(Mockito.any(OffsetDateTime.class));

    Mockito.when(service.findById(loanId)).thenReturn(Optional.of(loan));
    Mockito.when(service.save(loan)).thenReturn(loan);
    Mockito.when(bookService.incrementAvailableCopies(bookId)).thenReturn(1);

    var resp = useCase.returnBook(loanId);

    assertThat(resp).isNotNull();
    assertThat(returnedAtRef.get()).isNotNull();

    Mockito.verify(loan).setReturnedAt(Mockito.any(OffsetDateTime.class));
    Mockito.verify(service).save(loan);
    Mockito.verify(bookService).incrementAvailableCopies(bookId);
  }
}