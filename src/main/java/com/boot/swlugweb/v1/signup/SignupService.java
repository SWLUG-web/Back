package com.boot.swlugweb.v1.signup;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor

public class SignupService {
    //save 메서드 사용을 위한 repository 선언
    @Autowired
    private final SignupUserInfoRepository signupUserInfoRepository;
    @Autowired
    private final SignupUsersRepository signupUsersRepository;
    @Autowired
    private final SignupUserRuleTypeRepository signupUserRuleTypeRepository;
    private final PasswordEncoder passwordEncoder; //비밀번호 암호화 -> 추후 변경 가능성O

    // userInfo entitiy변경 메서드
    public SignupUserInfoDomain ConvertUserInfoToDomain(SignupRequestDto signupRequestDto){
        SignupUserInfoDomain signupUserInfoDomain = new SignupUserInfoDomain();
        signupUserInfoDomain.setUser_id(signupRequestDto.getUser_id());
        signupUserInfoDomain.setEmail(signupRequestDto.getEmail());
        signupUserInfoDomain.setPhone(signupRequestDto.getPhone());

        return signupUserInfoDomain;
    }

    // users entitiy변경 메서드
    public SignupUsersDomain ConvertUsersToDomain(SignupRequestDto signupRequestDto){
        SignupUsersDomain signupUsersDomain = new SignupUsersDomain();
        signupUsersDomain.setUser_id(signupRequestDto.getUser_id());
        signupUsersDomain.setPw(passwordEncoder.encode(signupRequestDto.getPw()));
        // test db에 있는 테이믈 맞춤
        signupUsersDomain.setId(0);
        signupUsersDomain.setAccount_locked(0);
        signupUsersDomain.setPassword("");
        signupUsersDomain.setTry_count(0);
        signupUsersDomain.setUserId("test111");


        return signupUsersDomain;
    }

    public SignupUserRuleTypeDomain ConvertUserRuleTypeToDomain(SignupRequestDto signupRequestDto){
        SignupUserRuleTypeDomain signupUserRuleTypeDomain = new SignupUserRuleTypeDomain();
        signupUserRuleTypeDomain.setUser_id(signupRequestDto.getUser_id());
        signupUserRuleTypeDomain.setNickname(signupRequestDto.getNickname());
        signupUserRuleTypeDomain.setRuleType(1);
        return signupUserRuleTypeDomain;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void registerUser(SignupRequestDto signupRequestDto) {

        System.out.println(signupRequestDto.getUser_id());

        //ID 중복 여부 확인
        if (signupUserInfoRepository.existsById(signupRequestDto.getUser_id())) {
            throw new IllegalArgumentException("이미 사용중인 ID입니다.");
        }

        //테이블이 양방향임!
        //1. 엔티티 설정
        SignupUserInfoDomain signupUserInfoDomain = ConvertUserInfoToDomain(signupRequestDto);
        SignupUsersDomain signupUsersDomain = ConvertUsersToDomain(signupRequestDto);
        SignupUserRuleTypeDomain signupUserRuleTypeDomain = ConvertUserRuleTypeToDomain(signupRequestDto);

        //2. 연관관계 설정
        signupUserInfoDomain.setSignupUserRuleType(signupUserRuleTypeDomain);
        signupUserInfoDomain.setSignupUsers(signupUsersDomain);

        System.out.println("users 테이블"+signupUsersDomain.getUserId());

        //3. 순차적으로 저장
        signupUserInfoRepository.save(signupUserInfoDomain);

        System.out.println("success to save the data in tables");

    }
}
