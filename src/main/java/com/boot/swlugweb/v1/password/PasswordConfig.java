package com.boot.swlugweb.v1.password;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class PasswordConfig {

    @Bean
    public SecurityFilterChain passwordFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/v1/password/**")
                .csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/password/request-reset").permitAll()
                        .requestMatchers("/api/v1/password/reset").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}