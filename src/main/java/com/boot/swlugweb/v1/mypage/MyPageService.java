package com.boot.swlugweb.v1.mypage;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MyPageService {
    private final MyPageRepository myPageRepository;

    public MyPageService(MyPageRepository myPageRepository) {
        this.myPageRepository = myPageRepository;
    }

    @Transactional(readOnly = true)
    public MyPageResponseDto getUserInfo(String userId) {
        return myPageRepository.findByUserId(userId)
                .map(userInfo -> new MyPageResponseDto(
                        userInfo.getSignupUsers().getUserId(),
                        userInfo.getSignupUserRuleType().getNickname(),
                        userInfo.getPhone(),
                        userInfo.getEmail()
                ))
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));
    }
}