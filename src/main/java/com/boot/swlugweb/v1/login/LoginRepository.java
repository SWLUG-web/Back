package com.boot.swlugweb.v1.login;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<LoginDomain, String> {
}