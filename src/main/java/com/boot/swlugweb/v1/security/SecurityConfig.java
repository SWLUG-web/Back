package com.boot.swlugweb.v1.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/blog/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/api/notice/save").hasRole("ADMIN")
                        .requestMatchers("/api/notice/delete").hasRole("ADMIN")
                        .requestMatchers("/api/notice/details").hasRole("ADMIN")
                        .requestMatchers("/api/notice/adjacent").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/api/notice/delete").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/api/login/**").permitAll()
                        .requestMatchers("/api/signup/**").permitAll()
                        .requestMatchers("/password/request-reset").permitAll()
                        .requestMatchers("/password/reset").permitAll()
                        .anyRequest().authenticated()

                )
                .formLogin(form -> form //로그인 과정 처리
                        .loginPage("http://localhost:3000/login") //login api 호출(front)
                        .loginProcessingUrl("/api/login/**") //로그인 요청 처리 url
                        .defaultSuccessUrl("http://localhost:3000/login") //로그인 성공 시 이동할 url (front)
                        .failureUrl("http://localhost:3000/login") //로그인 실패 시 이동할 url (front)
                        .permitAll()

                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
