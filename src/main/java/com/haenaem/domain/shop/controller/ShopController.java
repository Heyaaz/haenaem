package com.haenaem.domain.shop.controller;

import com.haenaem.domain.shop.dto.PurchaseResult;
import com.haenaem.domain.shop.dto.ShopDto;
import com.haenaem.domain.shop.service.ShopService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shop")
@RequiredArgsConstructor
@Slf4j
public class ShopController {

    private final ShopService shopService;

    /**
     * 모든 상품 조회
     */
    @GetMapping("/items")
    public ResponseEntity<List<ShopDto>> getAllItems() {
        List<ShopDto> items = shopService.getAllItems();
        return ResponseEntity.ok(items);
    }

    /**
     * 활성화된 상품만 조회
     */
    @GetMapping("/items/active")
    public ResponseEntity<List<ShopDto>> getActiveItems() {
        List<ShopDto> items = shopService.getActiveItems();
        return ResponseEntity.ok(items);
    }

    /**
     * 상품 상세 조회
     */
    @GetMapping("/items/{itemId}")
    public ResponseEntity<ShopDto> getItemById(@PathVariable Long itemId) {
        ShopDto item = shopService.getItemById(itemId);
        return ResponseEntity.ok(item);
    }

    /**
     * 아이템 구매
     */
    @PostMapping("/users/{userId}/items/{itemId}/purchase")
    public ResponseEntity<PurchaseResult> purchaseItem(
        @PathVariable Long userId,
        @PathVariable Long itemId) {
        PurchaseResult result = shopService.purchaseItem(userId, itemId);
        return ResponseEntity.ok(result);
    }
}