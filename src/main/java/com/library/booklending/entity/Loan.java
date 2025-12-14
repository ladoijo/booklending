package com.library.booklending.entity;

import com.library.booklending.dto.BorrowBookReqDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

@Table
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("is_deleted = false")
public class Loan extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "book_id", nullable = false)
  private Book book;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "member_id", nullable = false)
  private Member member;

  @Column(nullable = false)
  private OffsetDateTime borrowedAt;

  @Column(nullable = false)
  private LocalDate dueDate;

  private OffsetDateTime returnedAt;

  public Loan(BorrowBookReqDto reqDto) {
    this.book = new Book(reqDto.bookId());
    this.member = new Member(reqDto.memberId());
    this.borrowedAt = OffsetDateTime.now();
  }
}
