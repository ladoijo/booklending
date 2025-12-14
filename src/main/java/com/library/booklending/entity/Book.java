package com.library.booklending.entity;

import com.library.booklending.dto.BookReqDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Table
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("is_deleted = false")
public class Book extends BaseEntity {

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String author;

  @Column(unique = true, nullable = false, length = 13)
  private String isbn;

  @Column(nullable = false)
  private int totalCopies;

  @Column(nullable = false)
  private int availableCopies;

  public Book(BookReqDto reqDto) {
    if (reqDto.id() != null) {
      this.setId(reqDto.id());
    }
    this.title = reqDto.title();
    this.author = reqDto.author();
    this.isbn = reqDto.isbn();
    this.totalCopies = reqDto.totalCopies();
    this.availableCopies = reqDto.availableCopies();
  }

  public Book(Long id) {
    this.setId(id);
  }
}
