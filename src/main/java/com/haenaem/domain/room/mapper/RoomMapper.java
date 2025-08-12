package com.haenaem.domain.room.mapper;

import com.haenaem.domain.inventory.dto.InventoryItemDto;
import com.haenaem.domain.inventory.mapper.InventoryMapper;
import com.haenaem.domain.room.dto.RoomDto;
import com.haenaem.domain.room.entity.Room;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {InventoryMapper.class})
public interface RoomMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.nickname", target = "userNickname")
    @Mapping(target = "placedItems", ignore = true)  // 별도로 처리
    RoomDto toDto(Room room);

    default RoomDto toDto(Room room, List<InventoryItemDto> placedItems) {
        RoomDto dto = toDto(room);
        return RoomDto.builder()
            .id(dto.id())
            .userId(dto.userId())
            .userNickname(dto.userNickname())
            .name(dto.name())
            .placedItems(placedItems)
            .createdAt(dto.createdAt())
            .updatedAt(dto.updatedAt())
            .build();
    }
}