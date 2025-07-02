package com.haenaem.domain.user.service;

import com.haenaem.domain.user.dto.UserCreateRequest;
import com.haenaem.domain.user.dto.UserDto;
import com.haenaem.domain.user.dto.UserUpdateRequest;

public interface UserService {

  /**
   * 유저 생성
   */
  UserDto createUser(UserCreateRequest userCreateRequest);

  /**
   * 로그인
   */
  UserDto login(String email, String password);

  /**
   * 내 정보 조회
   */
  UserDto getMyInfo(String nickname);

  /**
   * 유저 정보 수정
   */
  UserDto updateUser(String nickname, UserUpdateRequest userUpdateRequest);
}
