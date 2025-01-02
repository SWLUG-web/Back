package com.boot.swlugweb.v1.login;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
public class LoginConfig {

    @Bean
    public SecurityFilterChain loginFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/v1/login/**")
                .csrf((csrf) -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(1) // 동시 세션 제한
                        .maxSessionsPreventsLogin(false) // 새로운 로그인 시 이전 세션 만료
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/login").permitAll()
                        .requestMatchers("/api/v1/login/check").permitAll()
                        .requestMatchers("/api/v1/login/logout").permitAll() // 로그아웃 경로 허용 추가
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    // HTTP 세션 이벤트 리스너 설정
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}