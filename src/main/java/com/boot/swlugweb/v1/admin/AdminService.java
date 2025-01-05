package com.boot.swlugweb.v1.admin;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {
    private final AdminUserInfoRepository adminUserInfoRepository;
    private final AdminUserTypeRepository adminUserTypeRepository;
    private final AdminUsersRepository adminUsersRepository;

    public AdminService(AdminUserInfoRepository adminUserInfoRepository, AdminUserTypeRepository adminUserTypeRepository, AdminUsersRepository adminUsersRepository) {
        this.adminUserInfoRepository = adminUserInfoRepository;
        this.adminUserTypeRepository = adminUserTypeRepository;
        this.adminUsersRepository = adminUsersRepository;
    }

    public List<AdminUserResponseDto> getAllUsers() {
        // 모든 AdminUserInfoDomain 데이터를 가져와서 필요한 형식으로 변환
        return adminUserInfoRepository.findAll().stream()
                .map(userInfo -> new AdminUserResponseDto(
                        userInfo.getUsersNum().getUsersNum(),
                        userInfo.getUsersNum().getUserId(),
                        userInfo.getEmail(),
                        userInfo.getPhone(),
                        userInfo.getUserType().getNickname(),
                        userInfo.getUserType().getRoleType()
                ))
                .collect(Collectors.toList());
    }

//    // 모든 데이터 삭제
//    @Transactional
//    public void deleteUserInfo(Integer usersInfoNum) {
//        AdminUserInfoDomain userInfo = adminUserInfoRepository.findByUsersNum(usersInfoNum)
//                .orElseThrow(() -> new EntityNotFoundException("User not found"));
//        System.out.println(userInfo);
//
//        // 부모 엔터티 삭제 -> 연관된 자식 엔터티도 자동 삭제됨
//        adminUserInfoRepository.delete(userInfo);
//    }
}
