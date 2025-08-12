package com.haenaem.domain.room.service;

import com.haenaem.domain.room.dto.ItemPlacementRequest;
import com.haenaem.domain.room.dto.RoomDto;

public interface RoomService {

  /**
   * 방 상태 조회
   */
  RoomDto getUserRoom(Long userId);

  /**
   * 아이템 배치/위치 이동
   */
  void placeItem(Long userId, ItemPlacementRequest request);

  /**
   * 아이템 배치 해제 (방에서 제거)
   */
  void removeItem(Long userId, Long inventoryItemId);
}
