package com.haenaem.domain.user.dto;

import lombok.Builder;

@Builder
public record UserCreateRequest(
    String email,
    String nickname,
    String password
) {

}
