package com.boot.swlugweb.v1.login;
//로그인
public class LoginResponseDto {
    private final boolean success;
    private final String message;
    private final String userId;

    public LoginResponseDto(boolean success, String message, String userId) {
        this.success = success;
        this.message = message;
        this.userId = userId;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getUserId() {
        return userId;
    }
}