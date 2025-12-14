package com.library.booklending.service;

import com.library.booklending.entity.Loan;
import com.library.booklending.repository.LoanRepository;
import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoanService {

  private final LoanRepository repository;

  @Value("${library.rules.borrow.max-active-loan}")
  private int maxActiveLoan;

  public Optional<Loan> findById(Long id) {
    return repository.findById(id);
  }

  public Loan save(Loan loan) {
    return repository.save(loan);
  }

  public boolean isLimitActiveLoansReached(Long memberId) {
    return repository.isLimitActiveLoansReached(memberId, maxActiveLoan);
  }

  public boolean hasOverdueActiveLoans(Long memberId) {
    var dateNow = LocalDate.now();
    return repository.hasOverdueActiveLoans(memberId, dateNow);
  }
}
