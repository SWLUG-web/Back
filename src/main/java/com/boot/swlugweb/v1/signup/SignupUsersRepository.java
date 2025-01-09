package com.boot.swlugweb.v1.signup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SignupUsersRepository extends JpaRepository<SignupUsersDomain, String> {
    Optional<SignupUsersDomain> findByUserId(String userId);
}

