package com.haenaem.config;

import com.haenaem.domain.user.entity.User;
import com.haenaem.domain.user.entity.UserRole;
import com.haenaem.domain.user.repository.UserRepository;
import com.haenaem.domain.inventory.entity.Inventory;
import com.haenaem.domain.inventory.repository.InventoryRepository;
import com.haenaem.domain.room.service.impl.RoomServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final InventoryRepository inventoryRepository;
    private final RoomServiceImpl roomService;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        createDefaultAdmin();
    }

    private void createDefaultAdmin() {
        String adminEmail = "admin@admin.com";
        
        if (userRepository.existsByEmail(adminEmail)) {
            log.info("기본 admin 계정이 이미 존재합니다: {}", adminEmail);
            return;
        }

        log.info("기본 admin 계정을 생성합니다: {}", adminEmail);

        User adminUser = User.builder()
            .email(adminEmail)
            .nickname("관리자")
            .password("admin123")
            .userRole(UserRole.ROLE_ADMIN)
            .image(null)
            .build();

        User savedAdmin = userRepository.save(adminUser);
        
        // 인벤토리 자동 생성
        Inventory inventory = new Inventory(savedAdmin);
        inventoryRepository.save(inventory);
        
        // 방 자동 생성
        roomService.createRoomForUser(savedAdmin);

        log.info("기본 admin 계정 생성 완료: {} (ID: {})", adminEmail, savedAdmin.getId());
    }
}