package com.haenaem.domain.inventory.service.impl;

import com.haenaem.domain.inventory.dto.InventoryItemDto;
import com.haenaem.domain.inventory.entity.Inventory;
import com.haenaem.domain.inventory.entity.InventoryItem;
import com.haenaem.domain.inventory.mapper.InventoryMapper;
import com.haenaem.domain.inventory.repository.InventoryItemRepository;
import com.haenaem.domain.inventory.repository.InventoryRepository;
import com.haenaem.domain.inventory.service.InventoryService;
import com.haenaem.domain.shop.entity.Shop;
import com.haenaem.domain.shop.repository.ShopRepository;
import com.haenaem.domain.user.entity.User;
import com.haenaem.domain.user.repository.UserRepository;
import com.haenaem.global.exception.DomainException;
import com.haenaem.global.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private final InventoryMapper inventoryMapper;

    @Override
    public void equipItem(Long userId, Long inventoryItemId) {
        InventoryItem inventoryItem = inventoryItemRepository.findById(inventoryItemId)
            .orElseThrow(() -> new DomainException(ErrorCode.INVENTORY_ITEM_NOT_FOUND));
        
        // 해당 아이템이 요청한 사용자의 것인지 검증
        if (!inventoryItem.getInventory().getUser().getId().equals(userId)) {
            throw new DomainException(ErrorCode.INVENTORY_ACCESS_DENIED);
        }
        
        inventoryItem.equip();
        inventoryItemRepository.save(inventoryItem);
    }

    @Override
    public void unequipItem(Long userId, Long inventoryItemId) {
        InventoryItem inventoryItem = inventoryItemRepository.findById(inventoryItemId)
            .orElseThrow(() -> new DomainException(ErrorCode.INVENTORY_ITEM_NOT_FOUND));
        
        // 해당 아이템이 요청한 사용자의 것인지 검증
        if (!inventoryItem.getInventory().getUser().getId().equals(userId)) {
            throw new DomainException(ErrorCode.INVENTORY_ACCESS_DENIED);
        }
        
        inventoryItem.unequip();
        inventoryItemRepository.save(inventoryItem);
    }

    @Override
    public InventoryItemDto addItemToInventory(Long userId, Long shopItemId) {
        Shop shopItem = shopRepository.findById(shopItemId)
            .orElseThrow(() -> new DomainException(ErrorCode.PURCHASE_ITEM_NOT_FOUND));
        
        Inventory inventory = getInventoryEntity(userId);
        InventoryItem inventoryItem = new InventoryItem(inventory, shopItem);
        inventory.addItem(inventoryItem);
        
        InventoryItem savedItem = inventoryItemRepository.save(inventoryItem);
        return inventoryMapper.toDto(savedItem);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryItemDto> getEquippedItems(Long userId) {
        List<InventoryItem> items = inventoryItemRepository.findByUserIdAndIsEquipped(userId, true);
        return inventoryMapper.toDtoList(items);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryItemDto> getUnequippedItems(Long userId) {
        List<InventoryItem> items = inventoryItemRepository.findByUserIdAndIsEquipped(userId, false);
        return inventoryMapper.toDtoList(items);
    }

    private Inventory getInventoryEntity(Long userId) {
        return inventoryRepository.findByUserId(userId)
            .orElseGet(() -> {
                User user = userRepository.findById(userId)
                    .orElseThrow(() -> new DomainException(ErrorCode.USER_NOT_FOUND));
                Inventory newInventory = new Inventory(user);
                return inventoryRepository.save(newInventory);
            });
    }
}