package com.library.booklending.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberReqDto(
    Long id,

    @NotBlank(message = "{error.member.name.empty}")
    String name,

    @NotBlank(message = "{error.member.email.empty}")
    @Email(message = "{error.member.email.invalid}")
    String email
) {

}
