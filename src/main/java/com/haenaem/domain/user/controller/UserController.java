package com.haenaem.domain.user.controller;

import com.haenaem.domain.user.dto.UserDto;
import com.haenaem.domain.user.dto.UserLoginRequest;
import com.haenaem.domain.user.dto.UserRegisterRequest;
import com.haenaem.domain.user.dto.UserUpdateRequest;
import com.haenaem.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Slf4j
public class UserController {

  private final UserService userService;

  /**
   * 유저 생성
   */
  @PostMapping
  public ResponseEntity<UserDto> register(@Valid @RequestBody UserRegisterRequest request) {
    log.info("유저 생성 요청: {}", request);
    UserDto userDto = userService.registerUser(request);
    log.info("유저 생성 완료: {}", userDto);
    return ResponseEntity.status(201).body(userDto);
  }

  /**
   * 로그인
   */
  @PostMapping("/login")
  public ResponseEntity<UserDto> login(@Valid @RequestBody UserLoginRequest request) {
    log.info("로그인 요청: {}", request);
    UserDto userDto = userService.login(request);
    log.info("로그인 완료: {}", userDto);
    return ResponseEntity.ok(userDto);
  }

  /**
   * 내 정보 조회
   */
  @GetMapping("/me/{userId}")
  public ResponseEntity<UserDto> getMyInfo(@PathVariable Long userId) {
    log.info("내 정보 조회 요청: userId={}", userId);
    UserDto userDto = userService.getMyInfo(userId);
    log.info("내 정보 조회 완료: {}", userDto);
    return ResponseEntity.ok(userDto);
  }

  /**
   * 유저 정보 수정
   */
  @PatchMapping("/update/{userId}")
  public ResponseEntity<UserDto> update(@PathVariable Long userId, @Valid @RequestBody UserUpdateRequest request) {
    log.info("유저 정보 수정 요청: {}", request);
    UserDto updatedUser = userService.updateUser(userId, request);
    log.info("유저 정보 수정 완료: {}", updatedUser);
    return ResponseEntity.ok(updatedUser);
  }

  /**
   * 유저 삭제
   */
  @DeleteMapping("/delete/{userId}")
  public ResponseEntity<Void> delete(@PathVariable Long userId) {
    log.info("유저 삭제 요청: userId={}", userId);
    userService.deleteUser(userId);
    log.info("유저 삭제 완료: userId={}", userId);
    return ResponseEntity.noContent().build();
  }

}
