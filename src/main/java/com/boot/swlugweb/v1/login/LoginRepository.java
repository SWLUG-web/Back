package com.boot.swlugweb.v1.login;

import org.springframework.data.jpa.repository.JpaRepository;
//로그인
public interface LoginRepository extends JpaRepository<LoginDomain, String> {
}