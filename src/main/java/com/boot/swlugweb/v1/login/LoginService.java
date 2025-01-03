package com.boot.swlugweb.v1.login;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class LoginService {
    private final LoginRepository loginRepository;
    private final PasswordEncoder passwordEncoder;
    private final HttpSession session;

    public LoginService(LoginRepository loginRepository,
                        PasswordEncoder passwordEncoder,
                        HttpSession session) {
        this.loginRepository = loginRepository;
        this.passwordEncoder = passwordEncoder;
        this.session = session;
    }

    public LoginResponseDto authenticateUser(String userId, String password) {
        Optional<LoginDomain> user = loginRepository.findById(userId);

        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPw())) {
            // 세션에 사용자 정보 저장
            session.setAttribute("USER", userId);
            return new LoginResponseDto(true, "Login successful", userId);
        }
        return new LoginResponseDto(false, "Invalid credentials", null);
    }

    public void logout() {
        session.invalidate();
    }

    public String getCurrentUser() {
        return (String) session.getAttribute("USER");
    }

    public boolean isLoggedIn() {
        return session.getAttribute("USER") != null;
    }
}