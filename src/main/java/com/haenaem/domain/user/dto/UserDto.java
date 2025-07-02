package com.haenaem.domain.user.dto;

import lombok.Builder;

@Builder
public record UserDto(
    Long id,
    String email,
    String nickname,
    long currentPoint
) {

}
