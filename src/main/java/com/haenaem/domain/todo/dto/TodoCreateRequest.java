package com.haenaem.domain.todo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record TodoCreateRequest(
    @NotBlank(message = "제목은 필수입니다.")
    @Size(min = 2, max = 50, message = "제목은 최소 2글자, 최대 50글자까지 입력할 수 있습니다.")
    String title,

    @NotBlank(message = "내용은 필수입니다.")
    @Size(min = 2, max = 500, message = "내용은 최소 2글자, 최대 500글자까지 입력할 수 있습니다.")
    String description
) {

}
