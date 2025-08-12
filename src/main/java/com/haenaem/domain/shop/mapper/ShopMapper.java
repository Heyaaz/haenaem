package com.haenaem.domain.shop.mapper;

import com.haenaem.domain.shop.dto.ShopDto;
import com.haenaem.domain.shop.entity.Shop;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShopMapper {

    ShopDto toDto(Shop shop);

    List<ShopDto> toDtoList(List<Shop> shops);
}