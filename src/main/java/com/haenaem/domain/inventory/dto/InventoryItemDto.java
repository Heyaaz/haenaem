package com.haenaem.domain.inventory.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InventoryItemDto {
    
    private Long id;
    private Long inventoryId;
    private Long shopItemId;
    private String shopItemName;
    private String shopItemDescription;
    private Integer shopItemPrice;
    private Boolean isEquipped;
    private String positionX;
    private String positionY;
    private String positionZ;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}