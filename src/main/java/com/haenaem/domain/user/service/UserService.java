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

  /**
   * 프로필 이미지 업데이트
   */
  UserDto updateProfileImage(Long userId, org.springframework.web.multipart.MultipartFile profileImage);

  /**
   * 사용자 권한 변경 (Admin 전용)
   */
  UserDto updateUserRole(Long adminUserId, Long targetUserId, com.haenaem.domain.user.entity.UserRole newRole);
}
