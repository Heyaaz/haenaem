package com.haenaem.domain.inventory.repository;

import com.haenaem.domain.inventory.entity.InventoryItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {

  List<InventoryItem> findByUserIdAndIsEquipped(Long userId, Boolean isEquipped);
}
