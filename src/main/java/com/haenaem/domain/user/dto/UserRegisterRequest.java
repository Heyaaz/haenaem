package com.haenaem.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UserRegisterRequest(
    @Email(message = "유효한 이메일 주소를 입력해주세요")
    @NotBlank(message = "유효한 이메일 주소를 입력해주세요")
    String email,

    @Size(min = 2, max = 20, message = "닉네임은 2자 이상 20자 이하로 입력해주세요.")
    @NotBlank(message = "닉네임은 최소 2자 이상이어야 합니다")
    String nickname,

    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
    @NotBlank(message = "비밀번호는 최소 8자 이상이어야 합니다")
    String password
) {

}
