package com.boot.swlugweb.v1.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminUserInfoRepository extends JpaRepository<AdminUserInfoDomain, Integer> {
    //Optional<AdminUsersDomain> findByUsersNum(Integer usersNum);
}
