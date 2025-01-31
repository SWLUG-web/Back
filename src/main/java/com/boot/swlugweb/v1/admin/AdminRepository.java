package com.boot.swlugweb.v1.admin;

import com.boot.swlugweb.v1.signup.SignupUserInfoDomain;
import com.boot.swlugweb.v1.signup.SignupUserRuleTypeDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<SignupUserInfoDomain, Integer> {
    @Query("SELECT ui FROM SignupUserInfoDomain ui " +
            "JOIN FETCH ui.signupUsers u " +
            "JOIN FETCH ui.signupUserRuleType ut " +
            "WHERE u.userId = :userId")
    Optional<SignupUserInfoDomain> findByUserId(@Param("userId") String userId);

    @Query("SELECT urt FROM SignupUserRuleTypeDomain urt")
    List<SignupUserRuleTypeDomain> findByUser();
}
