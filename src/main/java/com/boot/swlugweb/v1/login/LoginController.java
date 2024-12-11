package com.boot.swlugweb.v1.login;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/login") //URL 경로
public class LoginController {
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping //POST 요청 처리
    //HTTP 응답 상태코드 + 응답값 반환
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto) {
        boolean isAuthenticated = loginService.authenticateUser(
                loginRequestDto.getUserId(),
                loginRequestDto.getPassword()
        );

        if (isAuthenticated) { //
            return ResponseEntity.ok("Login successful!");
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}