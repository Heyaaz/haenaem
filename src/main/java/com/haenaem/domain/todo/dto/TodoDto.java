package com.haenaem.domain.todo.dto;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record TodoDto(
    Long id,
    String title,
    String content,
    boolean completed,
    LocalDateTime dueDate,
    Long userId
) {
}
