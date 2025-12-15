package com.library.booklending.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.library.booklending.dto.MemberReqDto;
import org.junit.jupiter.api.Test;

class MemberTest {

  @Test
  void constructor_fromReqDto_whenIdProvided_shouldCopyIdAndFields() {
    var dto =
        new MemberReqDto(
            10L,
            "Robert C. Martin",
            "robert@gmail.com"
        );

    var member = new Member(dto);
    assertEquals(10L, member.getId());
    assertEquals("Robert C. Martin", member.getName());
    assertEquals("robert@gmail.com", member.getEmail());
  }

  @Test
  void constructor_fromReqDto_whenIdNull_shouldCopyFieldsAndKeepIdNull() {
    var dto =
        new MemberReqDto(
            null,
            "Robert C. Martin",
            "robert@gmail.com"
        );

    var member = new Member(dto);
    assertEquals(0, member.getId());
    assertEquals("Robert C. Martin", member.getName());
    assertEquals("robert@gmail.com", member.getEmail());
  }

  @Test
  void constructor_withId_shouldSetId() {
    var member = new Member(123L);
    assertEquals(123L, member.getId());
  }
}

