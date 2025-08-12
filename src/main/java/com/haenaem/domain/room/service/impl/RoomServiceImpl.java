package com.haenaem.domain.room.service.impl;

import com.haenaem.domain.inventory.dto.InventoryItemDto;
import com.haenaem.domain.inventory.entity.InventoryItem;
import com.haenaem.domain.inventory.repository.InventoryItemRepository;
import com.haenaem.domain.inventory.service.InventoryService;
import com.haenaem.domain.room.dto.ItemPlacementRequest;
import com.haenaem.domain.room.dto.RoomDto;
import com.haenaem.domain.room.entity.Room;
import com.haenaem.domain.room.mapper.RoomMapper;
import com.haenaem.domain.room.repository.RoomRepository;
import com.haenaem.domain.room.service.RoomService;
import com.haenaem.domain.user.entity.User;
import com.haenaem.domain.user.repository.UserRepository;
import com.haenaem.global.exception.DomainException;
import com.haenaem.global.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final InventoryService inventoryService;
    private final RoomMapper roomMapper;

    @Override
    public RoomDto getUserRoom(Long userId) {
        log.debug("사용자 방 조회: userId={}", userId);
        
        Room room = roomRepository.findByUserId(userId)
            .orElseThrow(() -> new DomainException(ErrorCode.ROOM_NOT_FOUND));
        
        // 방에 배치된 아이템들 조회 (positionX, Y, Z가 모두 있는 아이템들)
        List<InventoryItemDto> placedItems = inventoryService.getEquippedItems(userId)
            .stream()
            .filter(item -> item.positionX() != null && item.positionY() != null && item.positionZ() != null)
            .toList();
        
        return roomMapper.toDto(room, placedItems);
    }

    @Transactional
    public RoomDto createRoomForUser(User user) {
        log.info("회원가입 시 방 생성: userId={}", user.getId());
        
        Room room = new Room(user, user.getNickname() + "의 방");
        Room savedRoom = roomRepository.save(room);
        
        return roomMapper.toDto(savedRoom, List.of());
    }

    @Override
    @Transactional
    public void placeItem(Long userId, ItemPlacementRequest request) {
        log.info("아이템 배치: userId={}, inventoryItemId={}, position=({}, {}, {})",
            userId, request.inventoryItemId(), request.positionX(), request.positionY(), request.positionZ());
        
        // 방 존재 확인
        roomRepository.findByUserId(userId)
            .orElseThrow(() -> new DomainException(ErrorCode.ROOM_NOT_FOUND));
        
        // 인벤토리 아이템 조회 및 권한 확인
        InventoryItem inventoryItem = inventoryItemRepository.findById(request.inventoryItemId())
            .orElseThrow(() -> new DomainException(ErrorCode.INVENTORY_ITEM_NOT_FOUND));
        
        if (!inventoryItem.getInventory().getUser().getId().equals(userId)) {
            throw new DomainException(ErrorCode.INVENTORY_ACCESS_DENIED);
        }
        
        // 위치 업데이트
        inventoryItem.updatePosition(request.positionX(), request.positionY(), request.positionZ());
        inventoryItemRepository.save(inventoryItem);
    }

    @Override
    @Transactional
    public void removeItem(Long userId, Long inventoryItemId) {
        log.info("아이템 배치 해제: userId={}, inventoryItemId={}", userId, inventoryItemId);
        
        // 방 존재 확인
        roomRepository.findByUserId(userId)
            .orElseThrow(() -> new DomainException(ErrorCode.ROOM_NOT_FOUND));
        
        // 인벤토리 아이템 조회 및 권한 확인
        InventoryItem inventoryItem = inventoryItemRepository.findById(inventoryItemId)
            .orElseThrow(() -> new DomainException(ErrorCode.INVENTORY_ITEM_NOT_FOUND));
        
        if (!inventoryItem.getInventory().getUser().getId().equals(userId)) {
            throw new DomainException(ErrorCode.INVENTORY_ACCESS_DENIED);
        }
        
        // 위치 정보 제거 (null로 설정)
        inventoryItem.updatePosition(null, null, null);
        inventoryItemRepository.save(inventoryItem);
    }
}