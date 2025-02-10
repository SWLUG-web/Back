package com.boot.swlugweb.v1.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .formLogin(form -> form.disable())
                // 세션 관리 설정 통합
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                )
                .authorizeHttpRequests(auth -> auth
                        // 블로그 관련 권한
                        // 공지사항 관련 권한
                        .requestMatchers("/api/blog/**").permitAll()
                        .requestMatchers("/api/notice/save").hasRole("ADMIN")
                        .requestMatchers("/api/notice/delete").hasRole("ADMIN")
                        .requestMatchers("/api/notice/details").hasAnyRole("ADMIN", "USER", "GUEST")
                        .requestMatchers("/api/notice/adjacent").hasAnyRole("ADMIN", "USER", "GUEST")
                        // 로그인/회원가입 관련 권한
                        .requestMatchers("/api/login/**").permitAll()
                        .requestMatchers("/api/login/check").permitAll()
                        .requestMatchers("/api/login/logout").permitAll()
                        .requestMatchers("/api/signup/**").permitAll()
                        .requestMatchers("/api/mypage").permitAll()
                        // 비밀번호 관련 권한
                        .requestMatchers("/api/password/request-reset").permitAll()
                        .requestMatchers("/api/password/reset").permitAll()
                        .requestMatchers("/api/password/verify").permitAll()
                        .requestMatchers("/api/password/verify-auth").permitAll()
                        .requestMatchers("/api/api/email/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        // Admin 페이지 관련 권한
                        .requestMatchers("/api/admin/**").permitAll()
                        
                );
        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // ✅ API 요청 허용
                        .allowedHeaders("*")
                        .allowedOrigins("http://localhost:3000") // ✅ 프론트엔드 주소 허용
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // ✅ 허용할 HTTP 메서드
                        .allowCredentials(true)
                        .exposedHeaders("Authorization");
            }
        };
    }

    // HTTP 세션 이벤트 리스너 설정 유지
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}
