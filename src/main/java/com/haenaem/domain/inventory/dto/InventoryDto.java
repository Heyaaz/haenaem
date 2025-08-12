package com.haenaem.domain.inventory.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
public record InventoryDto(
    Long id,
    Long userId,
    String userNickname,
    List<InventoryItemDto> items,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

}