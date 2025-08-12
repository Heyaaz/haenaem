package com.haenaem.domain.room.dto;

import lombok.Builder;

@Builder
public record ItemPlacementRequest(
    Long inventoryItemId,
    String positionX,
    String positionY,
    String positionZ
) {
}