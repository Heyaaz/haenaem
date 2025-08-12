package com.haenaem.domain.shop.service.impl;

import com.haenaem.domain.inventory.service.InventoryService;
import com.haenaem.domain.shop.dto.PurchaseResult;
import com.haenaem.domain.shop.dto.ShopDto;
import com.haenaem.domain.shop.entity.Shop;
import com.haenaem.domain.shop.mapper.ShopMapper;
import com.haenaem.domain.shop.repository.ShopRepository;
import com.haenaem.domain.shop.service.ShopService;
import com.haenaem.domain.user.entity.User;
import com.haenaem.domain.user.repository.UserRepository;
import com.haenaem.global.exception.DomainException;
import com.haenaem.global.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ShopServiceImpl implements ShopService {

  private final ShopRepository shopRepository;
  private final UserRepository userRepository;
  private final InventoryService inventoryService;
  private final ShopMapper shopMapper;

  @Transactional
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

    // 포인트 차감 로직 (먼저 차감)
    int cost = shop.getPrice();
    user.decreasePoint(cost);
    
    // 아이템을 인벤토리에 추가
    inventoryService.addItemToInventory(userId, itemId);
    log.info("아이템 구매 완료: userId={}, itemId={}, remainingPoint={}", userId, itemId, user.getCurrentPoint());

    return PurchaseResult.builder()
        .userId(userId)
        .itemId(itemId)
        .itemName(shop.getName())
        .itemPrice(shop.getPrice())
        .remainingPoint(user.getCurrentPoint())
        .build();
  }

  @Override
  public List<ShopDto> getAllItems() {
    log.debug("모든 상품 조회");
    List<Shop> shops = shopRepository.findAll();
    return shopMapper.toDtoList(shops);
  }

  @Override
  public List<ShopDto> getActiveItems() {
    log.debug("활성화된 상품 조회");
    List<Shop> shops = shopRepository.findByIsActiveTrue();
    return shopMapper.toDtoList(shops);
  }

  @Override
  public ShopDto getItemById(Long itemId) {
    log.debug("상품 조회: itemId={}", itemId);
    Shop shop = shopRepository.findById(itemId)
        .orElseThrow(() -> new DomainException(ErrorCode.PURCHASE_ITEM_NOT_FOUND));
    return shopMapper.toDto(shop);
  }
}
