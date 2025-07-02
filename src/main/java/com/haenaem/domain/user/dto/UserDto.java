package com.haenaem.domain.user.dto;

import com.haenaem.domain.user.entity.User;
import lombok.Builder;

@Builder
public record UserDto(
    Long id,
    String email,
    String nickname,
    int currentPoint
) {

  public static UserDto from(User user){
    return new UserDto(user.getId(), user.getEmail(), user.getNickname(),
        user.getCurrentPoint());
  }
}
