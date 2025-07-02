package com.haenaem.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserLoginRequest(
    @Email(message = "유효한 이메일 주소를 입력해주세요")
    @NotBlank(message = "유효한 이메일 주소를 입력해주세요")
    String email,

    @NotBlank(message = "비밀번호를 입력해주세요")
    String password
) {

}
