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
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                )
                .authorizeHttpRequests(auth -> auth
                        // 정적 리소스
                        .requestMatchers("/static/**", "/img/**", "/apply_swlug.png").permitAll()

                        // 공지사항 API - 조회 관련
                        .requestMatchers(
                                "/api/notice",
                                "/api/notice/detail",
                                "/api/notice/adjacent"
                        ).permitAll()

                        // 공지사항 API - 관리자 전용
                        .requestMatchers(
                                "/api/notice/save",
                                "/api/notice/update",
                                "/api/notice/delete",
                                "/api/notice/image/upload"
                        ).permitAll()

                        // 블로그 API
                        .requestMatchers("/api/blog/**").permitAll()

                        // 인증 관련 API
                        .requestMatchers(
                                "/api/login/**",
                                "/api/signup/**",
                                "/api/password/**"
                        ).permitAll()

                        // mypage API
                        .requestMatchers("/api/mypage/**").permitAll()

                        // 그 외 API
                        .requestMatchers("/api/**").permitAll()

                        .anyRequest().authenticated()
                );
        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // API 요청 허용
                        .allowedHeaders("*")
                        .allowedOrigins("http://localhost:3000") // 론트엔드 주소 허용
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") //허용할 HTTP 메서드
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