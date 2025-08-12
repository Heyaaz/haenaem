package com.haenaem.inventory.entity;

import static jakarta.persistence.GenerationType.IDENTITY;

import com.haenaem.domain.shop.entity.Shop;
import com.haenaem.domain.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Getter
@Table(name = "inventories")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Inventory {

  @Id @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "item_id", nullable = false)
  private List<Shop> itemId;

  @Column(name = "is_Equipped", nullable = false)
  private Boolean isEquipped = false;

  @Column(name = "position_x", nullable = false)
  private String positionX;

  @Column(name = "position_y", nullable = false)
  private String positionY;

  @Column(name = "position_z", nullable = false)
  private String positionZ; // 비쌀수록 앞에 표시

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

}
