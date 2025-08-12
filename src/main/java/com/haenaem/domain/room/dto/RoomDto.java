package com.haenaem.domain.room.dto;

import com.haenaem.domain.inventory.dto.InventoryItemDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record RoomDto(
    Long id,
    Long userId,
    String userNickname,
    String name,
    List<InventoryItemDto> placedItems,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}