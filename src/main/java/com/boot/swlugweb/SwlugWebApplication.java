package com.boot.swlugweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.context.annotation.ComponentScan;

import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {
		"com.boot.swlugweb.v1.login",
		"com.boot.swlugweb.v1.signup",
		"com.boot.swlugweb.v1.board",
		"com.boot.swlugweb.v1.mypage"
}) // JPA 레포지토리 경로 설정
@EntityScan(basePackages = {
		"com.boot.swlugweb.v1.login",
		"com.boot.swlugweb.v1.signup",
		"com.boot.swlugweb.v1.board",
		"com.boot.swlugweb.v1.mypage"
}) // 엔티티 클래스 경로 설정

//@ComponentScan(
//		excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.boot.swlugweb.v1.board.*")
//)


public class SwlugWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(SwlugWebApplication.class, args);
	}
}