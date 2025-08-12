package com.haenaem.domain.inventory.service;

import com.haenaem.domain.inventory.dto.InventoryItemDto;
import java.util.List;

public interface InventoryService {
    
    void equipItem(Long userId, Long inventoryItemId);
    
    void unequipItem(Long userId, Long inventoryItemId);
    
    InventoryItemDto addItemToInventory(Long userId, Long shopItemId);
    
    List<InventoryItemDto> getEquippedItems(Long userId);
    
    List<InventoryItemDto> getUnequippedItems(Long userId);
}