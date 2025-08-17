package com.haenaem.domain.user.service;

import com.haenaem.domain.user.dto.UserRegisterRequest;
import com.haenaem.domain.user.dto.UserDto;
import com.haenaem.domain.user.dto.UserLoginRequest;
import com.haenaem.domain.user.dto.UserUpdateRequest;
import com.haenaem.domain.user.entity.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

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
  UserDto updateProfileImage(Long userId, MultipartFile profileImage);

  /**
   * 사용자 권한 변경 (Admin 전용)
   */
  UserDto updateUserRole(Long adminUserId, Long targetUserId, UserRole newRole);

  /**
   * 모든 사용자 페이지네이션 조회
   */
  Page<UserDto> getAllUsers(Pageable pageable);

  /**
   * 키워드로 사용자 검색 (페이지네이션)
   */
  Page<UserDto> searchUsers(String keyword, Pageable pageable);
}
