package com.library.booklending.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.library.booklending.entity.Loan;
import com.library.booklending.repository.LoanRepository;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

  @Mock
  private LoanRepository repository;

  @InjectMocks
  private LoanService service;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(service, "maxActiveLoan", 3);
  }

  @Test
  void findById_shouldDelegateToRepository() {
    var id = 10L;
    var loan = mock(Loan.class);
    when(repository.findById(id)).thenReturn(Optional.of(loan));

    var result = service.findById(id);

    assertTrue(result.isPresent());
    assertSame(loan, result.get());
    verify(repository).findById(id);
    verifyNoMoreInteractions(repository);
  }

  @Test
  void save_shouldDelegateToRepository() {
    var loan = mock(Loan.class);
    var saved = mock(Loan.class);
    when(repository.save(loan)).thenReturn(saved);

    var result = service.save(loan);

    assertSame(saved, result);
    verify(repository).save(loan);
    verifyNoMoreInteractions(repository);
  }

  @Test
  void isLimitActiveLoansReached_shouldPassMemberIdAndMaxActiveLoan() {
    var memberId = 99L;
    when(repository.isLimitActiveLoansReached(memberId, 3)).thenReturn(true);

    var result = service.isLimitActiveLoansReached(memberId);

    assertTrue(result);
    verify(repository).isLimitActiveLoansReached(memberId, 3);
    verifyNoMoreInteractions(repository);
  }

  @Test
  void hasOverdueActiveLoans_shouldPassMemberIdAndTodayDate() {
    var memberId = 42L;
    when(repository.hasOverdueActiveLoans(eq(memberId), any(LocalDate.class))).thenReturn(false);

    var result = service.hasOverdueActiveLoans(memberId);

    assertFalse(result);

    ArgumentCaptor<LocalDate> dateCaptor = ArgumentCaptor.forClass(LocalDate.class);
    verify(repository).hasOverdueActiveLoans(eq(memberId), dateCaptor.capture());

    assertEquals(LocalDate.now(), dateCaptor.getValue());
    verifyNoMoreInteractions(repository);
  }
}