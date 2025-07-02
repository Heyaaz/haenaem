package com.haenaem.domain.user.service.impl;

import com.haenaem.domain.user.dto.UserCreateRequest;
import com.haenaem.domain.user.dto.UserDto;
import com.haenaem.domain.user.entity.User;
import com.haenaem.domain.user.mapper.UserMapper;
import com.haenaem.domain.user.repository.UserRepository;
import com.haenaem.domain.user.service.UserService;
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
  public UserDto createUser(UserCreateRequest request) {
    log.info("유저 생성 요청: {}", request);

    if(userRepository.exitsByEmail(request.email())) {
      log.debug("이미 존재하는 이메일: {}", request.email());
      throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
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
}
