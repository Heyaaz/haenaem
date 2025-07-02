package com.haenaem.domain.user.mapper;

import com.haenaem.domain.user.dto.UserDto;
import com.haenaem.domain.user.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
  /**
   * User 엔티티를 UserDto로 변환
   *
   * @param user User 엔티티
   * @return 변환된 UserDto
   */
  UserDto toDto(User user);
}
