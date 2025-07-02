package com.haenaem.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Custom Error 지정 필요성이 있다면 에러 코드를 작성하여 진행.
 *
 * <p>일반적인 상황의 에러만 적혀있지만, 특정 도메인에서 발생할 수 있는 에러 등 정의\n\n
 * CustomException(ErrorCode.INTERNAL_SERVER_ERROR) 처럼 사용.
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  // User
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.", "해당 사용자가 존재하지 않습니다."),
  USER_DUPLICATION(HttpStatus.CONFLICT, "중복된 이메일입니다.", "이미 존재하는 이메일입니다."),
  EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "이메일을 찾을 수 없습니다.", "존재하지 않는 이메일입니다."),

  // To do
  TODO_NOT_FOUND(HttpStatus.NOT_FOUND, "Todo를 찾을 수 없습니다.", "해당 Todo가 존재하지 않습니다."),

  // Shop
  PURCHASE_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "아이템을 찾을 수 없습니다.", "해당 아이템이 존재하지 않습니다."),
  PURCHASE_DEACTIVATED_ITEM(HttpStatus.BAD_REQUEST, "비활성화된 아이템입니다.", "해당 아이템은 비활성화되어 구매할 수 없습니다."),
  NOT_ENOUGH_POINT(HttpStatus.BAD_REQUEST, "포인트가 부족합니다.", "포인트가 부족하여 요청을 처리할 수 없습니다."),


  // COMMON,
  INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "잘못된 입력값입니다.", "잘못된 요청을 진행하였습니다."),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러입니다.", "관리자에게 연락해 주세요."),
  NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 자원을 찾을 수 없습니다.", "잘못된 요청을 진행하였습니다."),
  METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 메서드입니다.", "잘못된 요청을 진행하였습니다.");

  private final HttpStatus status;
  private final String message;
  private final String detail;
}