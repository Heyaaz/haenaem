package com.haenaem.domain.inventory.entity;

import static jakarta.persistence.GenerationType.IDENTITY;

import com.haenaem.domain.shop.entity.Shop;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Getter
@Table(name = "inventory_items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InventoryItem {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id", nullable = false)
    private Inventory inventory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_item_id", nullable = false)
    private Shop shopItem;

    @Column(name = "is_equipped", nullable = false)
    private Boolean isEquipped = false;

    @Column(name = "position_x")
    private String positionX;

    @Column(name = "position_y")
    private String positionY;

    @Column(name = "position_z")
    private String positionZ;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public InventoryItem(Inventory inventory, Shop shopItem) {
        this.inventory = inventory;
        this.shopItem = shopItem;
        this.isEquipped = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void equip() {
        this.isEquipped = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void unequip() {
        this.isEquipped = false;
        this.updatedAt = LocalDateTime.now();
    }

    public void updatePosition(String x, String y, String z) {
        this.positionX = x;
        this.positionY = y;
        this.positionZ = z;
        this.updatedAt = LocalDateTime.now();
    }
}