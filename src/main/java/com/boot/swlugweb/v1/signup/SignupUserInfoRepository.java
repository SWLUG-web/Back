package com.boot.swlugweb.v1.signup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
//회원가입
@Repository
public interface SignupUserInfoRepository extends JpaRepository<SignupUserInfoDomain, String> {
    Optional<SignupUserInfoDomain> findBySignupUsers(SignupUsersDomain users);
}
