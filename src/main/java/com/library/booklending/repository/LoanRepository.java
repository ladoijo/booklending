package com.library.booklending.repository;

import com.library.booklending.entity.Loan;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

  @Query("""
          SELECT CASE WHEN COUNT(loan) >= :maxActiveLoan THEN TRUE ELSE FALSE END
          FROM Loan loan
          WHERE loan.member.id = :memberId
              AND loan.returnedAt IS NULL
              AND loan.isDeleted = false
      """)
  boolean isLimitActiveLoansReached(Long memberId, int maxActiveLoan);

  @Query("""
          SELECT CASE WHEN COUNT(loan) > 0 THEN TRUE ELSE FALSE END
          FROM Loan loan
          WHERE loan.member.id = :memberId
              AND loan.returnedAt IS NULL
              AND loan.dueDate <= :dateNow
              AND loan.isDeleted = false
      """)
  boolean hasOverdueActiveLoans(Long memberId, LocalDate dateNow);
}
