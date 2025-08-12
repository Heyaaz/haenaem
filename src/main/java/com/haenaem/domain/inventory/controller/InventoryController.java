package com.haenaem.domain.inventory.controller;

import com.haenaem.domain.inventory.dto.InventoryItemDto;
import com.haenaem.domain.inventory.service.InventoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    /**
     * 아이템 장착
     * @param userId
     * @param inventoryItemId
     * @return
     */
    @PostMapping("/users/{userId}/items/{inventoryItemId}/equip")
    public ResponseEntity<Void> equipItem(@PathVariable Long userId, @PathVariable Long inventoryItemId) {
        inventoryService.equipItem(userId, inventoryItemId);
        return ResponseEntity.ok().build();
    }

    /**
     * 아이템 해제
     * @param userId
     * @param inventoryItemId
     * @return
     */
    @PostMapping("/users/{userId}/items/{inventoryItemId}/unequip")
    public ResponseEntity<Void> unequipItem(@PathVariable Long userId, @PathVariable Long inventoryItemId) {
        inventoryService.unequipItem(userId, inventoryItemId);
        return ResponseEntity.ok().build();
    }

    /**
     * 인벤토리에 추가
     * @param userId
     * @param shopItemId
     * @return
     */
    @PostMapping("/users/{userId}/items/{shopItemId}")
    public ResponseEntity<InventoryItemDto> addItemToInventory(
        @PathVariable Long userId, 
        @PathVariable Long shopItemId) {
        InventoryItemDto inventoryItem = inventoryService.addItemToInventory(userId, shopItemId);
        return ResponseEntity.ok(inventoryItem);
    }

    /**
     * 장착 아이템 목록 조회
     * @param userId
     * @return
     */
    @GetMapping("/users/{userId}/equipped")
    public ResponseEntity<List<InventoryItemDto>> getEquippedItems(@PathVariable Long userId) {
        List<InventoryItemDto> equippedItems = inventoryService.getEquippedItems(userId);
        return ResponseEntity.ok(equippedItems);
    }

    /**
     * 미장착 아이템 목록 조회
     * @param userId
     * @return
     */
    @GetMapping("/users/{userId}/unequipped")
    public ResponseEntity<List<InventoryItemDto>> getUnequippedItems(@PathVariable Long userId) {
        List<InventoryItemDto> unequippedItems = inventoryService.getUnequippedItems(userId);
        return ResponseEntity.ok(unequippedItems);
    }
}