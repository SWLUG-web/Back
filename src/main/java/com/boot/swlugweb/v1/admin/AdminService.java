package com.boot.swlugweb.v1.admin;

import com.boot.swlugweb.v1.signup.SignupUserRuleTypeDomain;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {
    private final AdminRepository adminRepository;
    private final AdminBlogRepository adminBlogRepository;

    public AdminService(AdminRepository adminRepository, AdminBlogRepository adminBlogRepository) {
        this.adminRepository = adminRepository;
        this.adminBlogRepository = adminBlogRepository;
    }

    public List<AdminUserResponseDto> getUsers() {
        List<SignupUserRuleTypeDomain> allUsers = adminRepository.findByUser();

        return allUsers.stream()
                .map(user -> new AdminUserResponseDto(
                        user.getTypeNum(),
                        user.getUserId(),
                        user.getNickname(),
                        user.getRole_type(),
                        user.getVersion()
                ))
                .collect(Collectors.toList());
    }

}
