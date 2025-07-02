package com.haenaem.domain.shop.dto;

import lombok.Builder;

@Builder
public record PurchaseResult(
    Long userId,
    Long itemId,
    String itemName,
    int itemPrice,
    int remainingPoint
) {

}
