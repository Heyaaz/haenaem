package com.haenaem.domain.inventory.mapper;

import com.haenaem.domain.inventory.dto.InventoryDto;
import com.haenaem.domain.inventory.dto.InventoryItemDto;
import com.haenaem.domain.inventory.entity.Inventory;
import com.haenaem.domain.inventory.entity.InventoryItem;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.nickname", target = "userNickname")
    InventoryDto toDto(Inventory inventory);

    @Mapping(source = "inventory.id", target = "inventoryId")
    @Mapping(source = "shopItem.id", target = "shopItemId")
    @Mapping(source = "shopItem.name", target = "shopItemName")
    @Mapping(source = "shopItem.description", target = "shopItemDescription")
    @Mapping(source = "shopItem.price", target = "shopItemPrice")
    InventoryItemDto toDto(InventoryItem inventoryItem);

    List<InventoryItemDto> toDtoList(List<InventoryItem> inventoryItems);
}