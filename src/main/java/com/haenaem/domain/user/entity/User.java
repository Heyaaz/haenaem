package com.haenaem.domain.user.entity;

import static jakarta.persistence.GenerationType.*;

import com.haenaem.domain.image.entity.Image;
import com.haenaem.domain.user.dto.UserUpdateRequest;
import com.haenaem.global.exception.DomainException;
import com.haenaem.global.exception.ErrorCode;
import com.haenaem.domain.inventory.entity.Inventory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity @Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class User {

  @Id @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private String nickname;

  @OneToOne
  @JoinColumn(name = "image_id")
  private Image image;

  @Column
  @Enumerated(EnumType.STRING)
  private UserRole role;

  @Column(nullable = false)
  private String password;

  @Column(name = "current_point")
  private int currentPoint = 0;

  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
  private Inventory inventory;

  @Builder
  private User(String email, String nickname, String password, Image image, UserRole userRole, int currentPoint) {
    this.email = email;
    this.nickname = nickname;
    this.password = password;
    this.image = image;
    this.role = userRole;
    this.currentPoint = 0;
  }


  public void update(UserUpdateRequest userUpdateRequest) {
    if (!this.nickname.equals(userUpdateRequest.nickname())) {
      this.nickname = userUpdateRequest.nickname();
    }
  }
  
  public void updateProfileImage(Image profileImage) {
    this.image = profileImage;
  }
  
  public void updateRole(UserRole newRole) {
    this.role = newRole;
  }

  public void decreasePoint(int cost) {
    if (currentPoint < cost) throw new DomainException(ErrorCode.NOT_ENOUGH_POINT);
      this.currentPoint -= cost;

  }
}
