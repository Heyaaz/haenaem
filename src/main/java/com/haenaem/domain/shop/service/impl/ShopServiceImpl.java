package com.haenaem.domain.shop.service.impl;

import com.haenaem.domain.shop.dto.PurchaseResult;
import com.haenaem.domain.shop.entity.Shop;
import com.haenaem.domain.shop.repository.ShopRepository;
import com.haenaem.domain.shop.service.ShopService;
import com.haenaem.domain.user.entity.User;
import com.haenaem.domain.user.repository.UserRepository;
import com.haenaem.global.exception.DomainException;
import com.haenaem.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class ShopServiceImpl implements ShopService {

  private final ShopRepository shopRepository;

  private final UserRepository userRepository;

  @Override
  public PurchaseResult purchaseItem(Long userId, Long itemId) {
    log.info("아이템 구매 요청: userId={}, itemId={}", userId, itemId);

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new DomainException(ErrorCode.USER_NOT_FOUND));

    Shop shop = shopRepository.findById(itemId)
        .orElseThrow(() -> new DomainException(ErrorCode.PURCHASE_ITEM_NOT_FOUND));

    if (!shop.isActive()) {
      log.debug("비활성화된 아이템 구매 시도: itemId={}", itemId);
      throw new DomainException(ErrorCode.PURCHASE_DEACTIVATED_ITEM);
    }

    if (shop.getPrice() > user.getCurrentPoint()) {
      log.debug("포인트 부족: userId={}, itemId={}, requiredPrice={}", userId, itemId, shop.getPrice());
      throw new DomainException(ErrorCode.NOT_ENOUGH_POINT);
    }

    // 아이템 구매 로직
    // TODO: inventory 구현 후 로직 추가


    return PurchaseResult.builder()
        .itemId(itemId)
        .itemName(shop.getName())
        .itemPrice(shop.getPrice())
        .remainingPoint(user.getCurrentPoint() - shop.getPrice())
        .build();
  }
}
