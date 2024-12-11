package com.boot.swlugweb.v1.login;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity //스프링 시큐리티 활성화
public class LoginConfig {  //보안 설정

    @Bean
    //보안 필터 체인 설정
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf.disable())  // CSRF 비활성화
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/login/**").permitAll()  //로그인 관련 URL은 모두에게 허용
                        .anyRequest().authenticated()  //나머지 URL은 인증 필요
                );

        return http.build();
    }
}