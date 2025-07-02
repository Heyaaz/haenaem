package com.haenaem.domain.user.service;

import com.haenaem.domain.user.dto.UserRegisterRequest;
import com.haenaem.domain.user.dto.UserDto;
import com.haenaem.domain.user.dto.UserLoginRequest;
import com.haenaem.domain.user.dto.UserUpdateRequest;

public interface UserService {

  /**
   * 유저 생성
   */
  UserDto registerUser(UserRegisterRequest userCreateRequest);

  /**
   * 로그인
   */
  UserDto login(UserLoginRequest userLoginRequest);

  /**
   * 내 정보 조회
   */
  UserDto getMyInfo(Long userId);

  /**
   * 유저 정보 수정
   */
  UserDto updateUser(Long userId, UserUpdateRequest userUpdateRequest);

  /**
   * 유저 삭제
   */
  void deleteUser(Long userId);
}
