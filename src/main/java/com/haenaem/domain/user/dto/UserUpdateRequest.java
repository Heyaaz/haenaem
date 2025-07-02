package com.haenaem.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
    @Size(min = 2, max = 20, message = "닉네임은 2자 이상 20자 이하로 입력해주세요.")
    @NotBlank(message = "닉네임은 최소 2자 이상이어야 합니다")
    String nickname
) {

}
