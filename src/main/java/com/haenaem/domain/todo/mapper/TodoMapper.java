package com.haenaem.domain.todo.mapper;

import com.haenaem.domain.todo.dto.TodoDto;
import com.haenaem.domain.todo.entity.Todo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TodoMapper {

  @Mapping(source = "user.id", target = "userId")
  TodoDto toDto(Todo todo);
}
