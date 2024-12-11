package com.boot.swlugweb.v1.login;

import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class LoginService {
    private final LoginRepository loginRepository;

    public LoginService(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    // 사용자 인증 메서드
    public boolean authenticateUser(String userId, String password) {
        Optional<LoginDomain> user = loginRepository.findById(userId);  //userId로 사용자 검색
        return user.map(u -> u.getPw().equals(password)).orElse(false); //비밀번호 일치 여부 확인 -> boolean 리턴
    }
}