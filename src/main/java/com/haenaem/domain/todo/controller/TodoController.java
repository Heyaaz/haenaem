package com.haenaem.domain.todo.controller;

import com.haenaem.domain.todo.dto.TodoCreateRequest;
import com.haenaem.domain.todo.dto.TodoDto;
import com.haenaem.domain.todo.dto.TodoUpdateRequest;
import com.haenaem.domain.todo.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/todos")
@Slf4j
public class TodoController {

  private final TodoService todoService;

  @PostMapping("/create")
  public ResponseEntity<TodoDto> createTodo(@RequestBody @Valid TodoCreateRequest request) {
    log.info("할 일 생성 요청: {}", request);
    TodoDto createdTodo = todoService.createTodo(request);
    log.info("할 일 생성 완료: {}", createdTodo);
    return ResponseEntity.status(201).body(createdTodo);
  }

  @GetMapping("/{todoId}")
  public ResponseEntity<TodoDto> getTodo(@PathVariable Long todoId) {
    log.info("할 일 조회 요청: todoId={}", todoId);
    TodoDto todo = todoService.getTodo(todoId);
    log.info("할 일 조회 완료: {}", todo);
    return ResponseEntity.ok(todo);
  }

  @PatchMapping("/update/{todoId}")
  public ResponseEntity<TodoDto> updateTodo(@PathVariable Long todoId, @RequestBody @Valid TodoUpdateRequest request) {
    log.info("할 일 수정 요청: {}", request);
    TodoDto updatedTodo = todoService.updateTodo(todoId, request);
    log.info("할 일 수정 완료: {}", updatedTodo);
    return ResponseEntity.ok(updatedTodo);
  }

  @DeleteMapping("/delete/{todoId}")
  public ResponseEntity<Void> deleteTodo(@PathVariable Long todoId) {
    log.info("할 일 삭제 요청: todoId={}", todoId);
    todoService.deleteTodo(todoId);
    log.info("할 일 삭제 완료: todoId={}", todoId);
    return ResponseEntity.noContent().build();
  }

}
