package com.haenaem.domain.user.service.impl;

import com.haenaem.domain.user.dto.UserRegisterRequest;
import com.haenaem.domain.user.dto.UserDto;
import com.haenaem.domain.user.dto.UserLoginRequest;
import com.haenaem.domain.user.dto.UserUpdateRequest;
import com.haenaem.domain.user.entity.User;
import com.haenaem.domain.user.mapper.UserMapper;
import com.haenaem.domain.user.repository.UserRepository;
import com.haenaem.domain.user.service.UserService;
import com.haenaem.global.exception.DomainException;
import com.haenaem.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  /**
   * 유저 생성
   */
  @Override
  public UserDto registerUser(UserRegisterRequest request) {
    log.info("유저 생성 요청: {}", request);

    if(userRepository.exitsByEmail(request.email())) {
      log.debug("이미 존재하는 이메일: {}", request.email());
      throw new DomainException(ErrorCode.USER_DUPLICATION);
    }

    User user = User.builder()
        .email(request.email())
        .nickname(request.nickname())
        .password(request.password()) // TODO: 비밀번호는 암호화 처리 필요
        .build();

    User savedUser = userRepository.save(user);

    log.info("유저 생성 완료: {}", savedUser);

    return userMapper.toDto(savedUser);
  }

  /**
   * 로그인
   */
  @Override
  public UserDto login(UserLoginRequest request) {
    log.info("로그인 요청: {}", request);

    String email = request.email();
    String password = request.password();

    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> {
          log.debug("존재하지 않는 이메일로 로그인 시도: {}", email);
          throw new DomainException(ErrorCode.EMAIL_NOT_FOUND);
        });

    if (!user.getPassword().equals(password)) {
      log.debug("비밀번호 불일치: 입력된 비밀번호={}, 저장된 비밀번호={}", password, user.getPassword());
      throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }

    log.info("로그인 성공: {}", user);

    return userMapper.toDto(user);
  }

  /**
   * 내 정보 조회
   */
  @Override
  public UserDto getMyInfo(long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> {
          log.debug("존재하지 않는 사용자 조회 시도: userId={}", userId);
          throw new DomainException(ErrorCode.USER_NOT_FOUND);
        });

    return UserDto.from(user);
  }

  /**
   * 유저 정보 수정
   */
  @Override
  public UserDto updateUser(long userId, UserUpdateRequest userUpdateRequest) {
    log.info("유저 정보 수정 요청: userId={}, request={}", userId, userUpdateRequest);

    User user = userRepository.findById(userId)
        .orElseThrow(() -> {
          log.debug("존재하지 않는 사용자 수정 시도: userId={}", userId);
          throw new DomainException(ErrorCode.USER_NOT_FOUND);
        });

    log.info("기존 유저 정보: {}", user.getNickname());
    user.update(userUpdateRequest);
    log.info("유저 정보 수정 완료: {}", user.getNickname());

    return userMapper.toDto(user);
  }

  /**
   * 유저 삭제
   */
  @Override
  public void deleteUser(long userId) {
    log.info("유저 삭제 요청: userId={}", userId);

    User user = userRepository.findById(userId)
        .orElseThrow(() -> {
          log.debug("존재하지 않는 사용자 삭제 시도: userId={}", userId);
          throw new DomainException(ErrorCode.USER_NOT_FOUND);
        });

    userRepository.delete(user);
  }
}
