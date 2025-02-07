package com.boot.swlugweb.v1.security;

import com.boot.swlugweb.v1.login.LoginRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class SecurityUserPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public SecurityUserPasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        setFilterProcessesUrl("/api/login"); //security config의 login url과 맞추기
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        try {

            LoginRequestDto loginRequestDto = objectMapper.readValue(request.getInputStream(), LoginRequestDto.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.getUserId(),
                            loginRequestDto.getPassword(),
                            Collections.emptyList()
                    )
            );
        } catch (IOException e) {
            throw new AuthenticationServiceException("failed to parse authentication request", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) authResult.getPrincipal();

        // 사용자의 첫 번째 권한을 가져와서 세션에 저장
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("");
        HttpSession session = request.getSession();
        session.setAttribute("ROLE", role);

        // 인증 성공 시 JSON 응답 구성
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(
                Map.of(
                        "success", true,
                        "userId", userDetails.getUsername(),
                        "role", role
                )
        ));

        // 필터 체인을 계속 진행하지 않고 여기서 종료
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(
                Map.of(
                        "success", false,
                        "message", failed.getMessage()
                )
        ));
    }


}
