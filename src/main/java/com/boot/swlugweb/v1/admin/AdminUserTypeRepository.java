package com.boot.swlugweb.v1.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminUserTypeRepository extends JpaRepository<AdminUserTypeDomain, String> {
}
