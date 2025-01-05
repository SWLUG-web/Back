package com.boot.swlugweb.v1.admin;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {
    private final AdminUserInfoRepository adminUserInfoRepository;

    public AdminService(AdminUserInfoRepository adminUserInfoRepository) {
        this.adminUserInfoRepository = adminUserInfoRepository;
    }

    public List<AdminUserResponseDto> getAllUsers() {
        // 모든 AdminUserInfoDomain 데이터를 가져와서 필요한 형식으로 변환
        return adminUserInfoRepository.findAll().stream()
                .map(userInfo -> new AdminUserResponseDto(
                        userInfo.getUsersNum().getUserId(),
                        userInfo.getEmail(),
                        userInfo.getPhone(),
                        userInfo.getUserType().getNickname(),
                        userInfo.getUserType().getRoleType()
                ))
                .collect(Collectors.toList());
    }
}
