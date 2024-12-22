package com.boot.swlugweb.v1.signup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignupUserInfoRepository  extends JpaRepository<SignupUserInfoDomain, String> {
}
