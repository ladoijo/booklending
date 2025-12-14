package com.library.booklending.entity;

import com.library.booklending.dto.MemberReqDto;
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
public class Member extends BaseEntity {

  @Column(nullable = false)
  private String name;

  @Column(unique = true, nullable = false, length = 50)
  private String email;

  public Member(MemberReqDto reqDto) {
    if (reqDto.id() != null) {
      this.setId(reqDto.id());
    }
    this.name = reqDto.name();
    this.email = reqDto.email();
  }

  public Member(Long id) {
    this.setId(id);
  }
}
