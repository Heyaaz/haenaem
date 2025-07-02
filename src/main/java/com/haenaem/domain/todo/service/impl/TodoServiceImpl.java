package com.haenaem.domain.todo.service.impl;

import com.haenaem.domain.todo.dto.TodoCreateRequest;
import com.haenaem.domain.todo.dto.TodoDto;
import com.haenaem.domain.todo.dto.TodoUpdateRequest;
import com.haenaem.domain.todo.entity.Todo;
import com.haenaem.domain.todo.mapper.TodoMapper;
import com.haenaem.domain.todo.repository.TodoRepository;
import com.haenaem.domain.todo.service.TodoService;
import com.haenaem.global.exception.DomainException;
import com.haenaem.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

  private final TodoRepository todoRepository;
  private final TodoMapper todoMapper;

  /**
   * 할 일 생성
   * @param todoCreateRequest
   * @return
   */
  @Override
  public TodoDto createTodo(TodoCreateRequest todoCreateRequest) {
    log.info("할 일 생성 요청: {}", todoCreateRequest);

    Todo todo = Todo.builder()
        .title(todoCreateRequest.title())
        .description(todoCreateRequest.description())
        .isCompleted(false)
        .dueDate(todoCreateRequest.dueDate())
        .pointsEarned(todoCreateRequest.earnedPoint()) // TODO : 포인트 랜덤 생성, 포인트 적립 로직 추가 필요
        .build();

    Todo savedTodo = todoRepository.save(todo);
    log.info("할 일 생성 완료: {}", savedTodo);
    return todoMapper.toDto(savedTodo);
  }

  /**
   * 할 일 조회
   * @param todoId
   * @return
   */
  @Override
  public TodoDto getTodo(Long todoId) {
    log.info("할 일 조회 요청: todoId={}", todoId);

    Todo todo = todoRepository.findById(todoId)
        .orElseThrow(() -> new DomainException(ErrorCode.TODO_NOT_FOUND));

    log.info("할 일 조회 완료: {}", todo);
    return todoMapper.toDto(todo);
  }

  /**
   * 할 일 수정
   * @param todoId
   * @param todoUpdateRequest
   * @return
   */
  @Override
  public TodoDto updateTodo(Long todoId, TodoUpdateRequest todoUpdateRequest) {
    log.info("할 일 수정 요청: todoId={}, todoUpdateRequest={}", todoId, todoUpdateRequest);

    Todo todo = todoRepository.findById(todoId)
        .orElseThrow(() -> new DomainException(ErrorCode.TODO_NOT_FOUND));

    // 수정할 필드만 업데이트
    todo.updateTitle(todoUpdateRequest.title());
    todo.updateDescription(todoUpdateRequest.description());

    Todo updatedTodo = todoRepository.save(todo);
    log.info("할 일 수정 완료: {}", updatedTodo);
    return todoMapper.toDto(updatedTodo);
  }

  /**
   * 할 일 삭제
   * @param todoId
   */
  @Override
  public void deleteTodo(Long todoId) {
    log.info("할 일 삭제 요청: todoId={}", todoId);

    Todo todo = todoRepository.findById(todoId)
        .orElseThrow(() -> new DomainException(ErrorCode.TODO_NOT_FOUND));

    todoRepository.delete(todo);
    log.info("할 일 삭제 완료: todoId={}", todoId);
  }
}
