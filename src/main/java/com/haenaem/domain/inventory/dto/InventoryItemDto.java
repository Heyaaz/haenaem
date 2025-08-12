package com.haenaem.domain.inventory.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
public record InventoryItemDto(
    Long id,
    Long inventoryId,
    Long shopItemId,
    String shopItemName,
    String shopItemDescription,
    Integer shopItemPrice,
    Boolean isEquipped,
    String positionX,
    String positionY,
    String positionZ,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}