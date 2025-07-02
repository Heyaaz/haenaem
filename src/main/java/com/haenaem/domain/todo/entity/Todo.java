package com.haenaem.domain.todo.entity;

import static jakarta.persistence.GenerationType.IDENTITY;

import com.haenaem.domain.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Getter
@Table(name = "todos")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Todo {

  @Id @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(nullable = false)
  private String title;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(name = "is_completed", nullable = false)
  private boolean isCompleted;

  @Column(name = "due_date")
  private LocalDateTime dueDate;

  @Column(name = "points_earned", nullable = false)
  private long pointsEarned;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @Builder
  public Todo(String title, String description, boolean isCompleted) {
    this.title = title;
    this.description = description;
    this.isCompleted = false;
  }

  public void updateTitle(String title) {
    if (title != null && !title.trim().isEmpty()) {
      this.title = title;
    }
  }

  public void updateDescription(String description) {
    if (description != null && !description.trim().isEmpty()) {
      this.description = description;
    }
  }
}
