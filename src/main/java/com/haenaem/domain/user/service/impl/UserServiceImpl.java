package com.haenaem.domain.user.service.impl;

import com.haenaem.domain.user.dto.UserCreateRequest;
import com.haenaem.domain.user.dto.UserDto;
import com.haenaem.domain.user.dto.UserLoginRequest;
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
          throw new IllegalArgumentException("존재하지 않는 이메일입니다.");
        });

    if (!user.getPassword().equals(password)) {
      log.debug("비밀번호 불일치: 입력된 비밀번호={}, 저장된 비밀번호={}", password, user.getPassword());
      throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }

    log.info("로그인 성공: {}", user);

    return userMapper.toDto(user);
  }


}
