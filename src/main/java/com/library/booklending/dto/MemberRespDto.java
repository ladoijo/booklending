package com.library.booklending.dto;

import com.library.booklending.entity.Member;

public record MemberRespDto(
    long id,
    String name,
    String email
) {

  public MemberRespDto(Member member) {
    this(member.getId(), member.getName(), member.getEmail());
  }
}
