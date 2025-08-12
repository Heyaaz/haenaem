package com.haenaem.domain.inventory.repository;

import com.haenaem.domain.inventory.entity.InventoryItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {

    @Query("SELECT ii FROM InventoryItem ii WHERE ii.inventory.user.id = :userId AND ii.isEquipped = :isEquipped")
    List<InventoryItem> findByUserIdAndIsEquipped(@Param("userId") Long userId, @Param("isEquipped") Boolean isEquipped);
}
