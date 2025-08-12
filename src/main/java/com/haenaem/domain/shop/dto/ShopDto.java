package com.haenaem.domain.shop.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
public record ShopDto(
    Long id,
    String name,
    String description,
    Integer price,
    Boolean isActive,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}