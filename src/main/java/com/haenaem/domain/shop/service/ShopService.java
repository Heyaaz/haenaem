package com.haenaem.domain.shop.service;

import com.haenaem.domain.shop.dto.PurchaseResult;
import com.haenaem.domain.shop.dto.ShopDto;
import java.util.List;

public interface ShopService {

  /**
   * 아이템 구매
   */
  PurchaseResult purchaseItem(Long userId, Long itemId);

  /**
   * 모든 상품 조회
   */
  List<ShopDto> getAllItems();

  /**
   * 활성화된 상품만 조회
   */
  List<ShopDto> getActiveItems();

  /**
   * 상품 ID로 조회
   */
  ShopDto getItemById(Long itemId);

}
