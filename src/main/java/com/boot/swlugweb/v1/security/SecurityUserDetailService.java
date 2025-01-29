package com.boot.swlugweb.v1.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class SecurityUserDetailService implements UserDetailsService {

    private final SecurityUserInfoRepository securityUserInfoRepository;
    public SecurityUserDetailService(SecurityUserInfoRepository securityUserInfoRepository) {
        this.securityUserInfoRepository = securityUserInfoRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //user_id로 사용자 정보 조회

        SecurityUserInfoDomain userInfoDomain = securityUserInfoRepository.findBySecurityUsers_userId(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with user_id: " + username));


        //권한 생성
        String role = "ROLE_" +  mapRoleTypetoString(userInfoDomain.getSecurityUserRuleType().getRole_type());
        return new org.springframework.security.core.userdetails.User(
                userInfoDomain.getSecurityUsers().getUserId(),
                userInfoDomain.getSecurityUsers().getPw(),
                Collections.singletonList(new SimpleGrantedAuthority(role))
        );
    }

    private String mapRoleTypetoString(int roleType) {
        switch (roleType) {
            case 0: return "GUEST";
            case 1: return "USER";
            case 2: return "ADMIN";
            default: throw new IllegalArgumentException("Invalid rule type: " + roleType);
        }
    }


}

