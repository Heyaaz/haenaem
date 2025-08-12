package com.haenaem.domain.inventory.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InventoryDto {
    
    private Long id;
    private Long userId;
    private String userNickname;
    private List<InventoryItemDto> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}