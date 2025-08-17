package com.haenaem.domain.user.service.impl;

import com.haenaem.domain.user.dto.UserRegisterRequest;
import com.haenaem.domain.user.dto.UserDto;
import com.haenaem.domain.user.dto.UserLoginRequest;
import com.haenaem.domain.user.dto.UserUpdateRequest;
import com.haenaem.domain.user.entity.User;
import com.haenaem.domain.user.mapper.UserMapper;
import com.haenaem.domain.user.repository.UserRepository;
import com.haenaem.domain.user.service.UserService;
import com.haenaem.domain.user.entity.UserRole;
import com.haenaem.domain.room.service.impl.RoomServiceImpl;
import com.haenaem.domain.inventory.entity.Inventory;
import com.haenaem.domain.inventory.repository.InventoryRepository;
import com.haenaem.domain.image.entity.Image;
import com.haenaem.domain.image.service.ImageService;
import com.haenaem.global.exception.DomainException;
import com.haenaem.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final RoomServiceImpl roomService;
  private final InventoryRepository inventoryRepository;
  private final ImageService imageService;

  /**
   * 유저 생성
   * @param request
   * @return
   */
  @Transactional
  @Override
  public UserDto registerUser(UserRegisterRequest request) {
    log.info("유저 생성 요청: {}", request);

    if(userRepository.existsByEmail(request.email())) {
      log.debug("이미 존재하는 이메일: {}", request.email());
      throw new DomainException(ErrorCode.USER_DUPLICATION);
    }

    // 프로필 이미지 처리
    Image profileImage = null;
    if (request.profileImage() != null && !request.profileImage().isEmpty()) {
      profileImage = imageService.upload(request.profileImage());
    }
    
    // 기본 Role 설정 (항상 ROLE_USER로 설정)
    UserRole userRole = UserRole.ROLE_USER;
    
    User user = User.builder()
        .email(request.email())
        .nickname(request.nickname())
        .password(request.password()) // TODO: 비밀번호는 암호화 처리 필요
        .image(profileImage)
        .userRole(userRole)
        .build();

    User savedUser = userRepository.save(user);
    
    // 인벤토리 자동 생성
    Inventory inventory = new Inventory(savedUser);
    inventoryRepository.save(inventory);
    
    // 방 자동 생성
    roomService.createRoomForUser(savedUser);

    log.info("유저 생성 완료 (인벤토리, 방 포함): {}", savedUser);

    return userMapper.toDto(savedUser);
  }

  /**
   * 로그인
   * @param request
   * @return
   */
  @Transactional
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
   * @param userId
   * @return
   */
  @Override
  public UserDto getMyInfo(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> {
          log.debug("존재하지 않는 사용자 조회 시도: userId={}", userId);
          throw new DomainException(ErrorCode.USER_NOT_FOUND);
        });

    return UserDto.from(user);
  }

  /**
   * 유저 정보 수정
   * @param userId
   * @param userUpdateRequest
   * @return
   */
  @Transactional
  @Override
  public UserDto updateUser(Long userId, UserUpdateRequest userUpdateRequest) {
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
  @Transactional
  @Override
  public void deleteUser(Long userId) {
    log.info("유저 삭제 요청: userId={}", userId);

    User user = userRepository.findById(userId)
        .orElseThrow(() -> {
          log.debug("존재하지 않는 사용자 삭제 시도: userId={}", userId);
          throw new DomainException(ErrorCode.USER_NOT_FOUND);
        });

    userRepository.delete(user);
  }

  @Override
  @Transactional
  public UserDto updateProfileImage(Long userId, MultipartFile profileImage) {
    log.info("프로필 이미지 업데이트 요청: userId={}", userId);

    User user = userRepository.findById(userId)
        .orElseThrow(() -> {
          log.debug("존재하지 않는 사용자 프로필 이미지 업데이트 시도: userId={}", userId);
          return new DomainException(ErrorCode.USER_NOT_FOUND);
        });

    // 기존 이미지 삭제 (있는 경우)
    if (user.getImage() != null) {
      imageService.delete(user.getImage().getId());
    }

    // 새 이미지 업로드 및 업데이트
    Image newProfileImage = imageService.upload(profileImage);
    user.updateProfileImage(newProfileImage);

    log.info("프로필 이미지 업데이트 완료: userId={}", userId);

    return userMapper.toDto(user);
  }

  @Override
  @Transactional
  public UserDto updateUserRole(Long adminUserId, Long targetUserId, UserRole newRole) {
    log.info("사용자 권한 변경 요청: adminUserId={}, targetUserId={}, newRole={}", adminUserId, targetUserId, newRole);
    
    // Admin 권한 확인
    User adminUser = userRepository.findById(adminUserId)
        .orElseThrow(() -> {
          log.debug("존재하지 않는 관리자: adminUserId={}", adminUserId);
          return new DomainException(ErrorCode.USER_NOT_FOUND);
        });
    
    if (adminUser.getRole() != UserRole.ROLE_ADMIN) {
      log.debug("권한 없는 사용자가 역할 변경 시도: adminUserId={}, role={}", adminUserId, adminUser.getRole());
      throw new DomainException(ErrorCode.ACCESS_DENIED);
    }
    
    // 대상 사용자 조회 및 권한 변경
    User targetUser = userRepository.findById(targetUserId)
        .orElseThrow(() -> {
          log.debug("존재하지 않는 대상 사용자: targetUserId={}", targetUserId);
          return new DomainException(ErrorCode.USER_NOT_FOUND);
        });
    
    UserRole oldRole = targetUser.getRole();
    targetUser.updateRole(newRole);
    
    log.info("사용자 권한 변경 완료: userId={}, {} -> {}", targetUserId, oldRole, newRole);
    
    return userMapper.toDto(targetUser);
  }

  /**
   * 모든 사용자 페이지네이션 조회
   */
  @Override
  public Page<UserDto> getAllUsers(Pageable pageable) {
    log.info("사용자 전체 조회 요청: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
    
    Page<User> userPage = userRepository.findAllWithPaging(pageable);
    
    log.info("사용자 전체 조회 완료: totalElements={}, totalPages={}", userPage.getTotalElements(), userPage.getTotalPages());
    
    return userPage.map(userMapper::toDto);
  }

  /**
   * 키워드로 사용자 검색 (페이지네이션)
   */
  @Override
  public Page<UserDto> searchUsers(String keyword, Pageable pageable) {
    log.info("사용자 검색 요청: keyword={}, page={}, size={}", keyword, pageable.getPageNumber(), pageable.getPageSize());
    
    Page<User> userPage = userRepository.findByKeywordWithPaging(keyword, pageable);
    
    log.info("사용자 검색 완료: keyword={}, totalElements={}, totalPages={}", keyword, userPage.getTotalElements(), userPage.getTotalPages());
    
    return userPage.map(userMapper::toDto);
  }
}
