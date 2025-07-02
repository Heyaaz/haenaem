package com.haenaem.domain.todo.service;

import com.haenaem.domain.todo.dto.TodoCreateRequest;
import com.haenaem.domain.todo.dto.TodoDto;
import com.haenaem.domain.todo.dto.TodoUpdateRequest;

public interface TodoService {

  /**
   * 할 일 생성
   */
  TodoDto createTodo(TodoCreateRequest todoCreateRequest);

  /**
   * 할 일 조회
   */
  TodoDto getTodo(Long todoId);

  /**
   * 할 일 수정
   */
  TodoDto updateTodo(Long todoId, TodoUpdateRequest todoUpdateRequest);

  /**
   * 할 일 삭제
   */
  void deleteTodo(Long todoId);

  /**
   * 할 일 완료 상태 변경
   * TODO: 생각 중
   */

}
