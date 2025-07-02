package com.haenaem.domain.user.dto;

public record UserLoginRequest(
    String email,
    String password
) {

}
