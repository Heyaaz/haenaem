package com.haenaem.domain.shop.service;

import com.haenaem.domain.shop.dto.PurchaseResult;

public interface ShopService {

  /**
   * 아이템 구매
   */
  PurchaseResult purchaseItem(Long userId, Long itemId);

}
