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
    public SignupUserInfoDomain ConvertUserInfoToDomain(SignupRequestDto signupRequestDto, SignupUsersDomain signupUsersDomain, SignupUserRuleTypeDomain signupUserRuleTypeDomain){
        SignupUserInfoDomain signupUserInfoDomain = new SignupUserInfoDomain();
        signupUserInfoDomain.setEmail(signupRequestDto.getEmail());
        signupUserInfoDomain.setPhone(signupRequestDto.getPhone());
        signupUserInfoDomain.setSignupUsers(signupUsersDomain); // FK 매핑
        signupUserInfoDomain.setSignupUserRuleType(signupUserRuleTypeDomain); // FK 매핑

        return signupUserInfoDomain;
    }

    // users entitiy변경 메서드
    private SignupUsersDomain convertUsersToDomain(SignupRequestDto signupRequestDto) {
        SignupUsersDomain signupUsersDomain = new SignupUsersDomain();
        signupUsersDomain.setPw(passwordEncoder.encode(signupRequestDto.getPw())); // 비밀번호 암호화
        signupUsersDomain.setUserId(signupRequestDto.getUserId());
        return signupUsersDomain;
    }

    // userRuleType Entity 변환 메서드
    private SignupUserRuleTypeDomain convertUserRuleTypeToDomain(SignupRequestDto signupRequestDto) {
        SignupUserRuleTypeDomain signupUserRuleTypeDomain = new SignupUserRuleTypeDomain();
        signupUserRuleTypeDomain.setNickname(signupRequestDto.getNickname());
        signupUserRuleTypeDomain.setRole_type(1); // 기본 ruleType 설정
        signupUserRuleTypeDomain.setUserId(signupRequestDto.getUserId());
        return signupUserRuleTypeDomain;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void registerUser(SignupRequestDto signupRequestDto) {

        //System.out.println(signupRequestDto.getUserId());

        //ID 중복 여부 확인
//        if (signupUserInfoRepository.existsById(signupRequestDto.getUser_id())) {
//            throw new IllegalArgumentException("이미 사용중인 ID입니다.");
//        }

        //users -> rule_type -> user_info 순 저장
        // 1. Users 테이블 저장
        SignupUsersDomain signupUsersDomain = convertUsersToDomain(signupRequestDto);

        System.out.println("users id:"+signupUsersDomain.getUserId());
        signupUsersDomain = signupUsersRepository.save(signupUsersDomain);

        System.out.println("save");

        // 2. UserRuleType 테이블 저장
        SignupUserRuleTypeDomain signupUserRuleTypeDomain = convertUserRuleTypeToDomain(signupRequestDto);
        System.out.println("user_type id:"+signupUserRuleTypeDomain.getUserId());
        signupUserRuleTypeDomain = signupUserRuleTypeRepository.save(signupUserRuleTypeDomain);
        System.out.println("save");


        // 3. UserInfo 테이블 저장
        SignupUserInfoDomain signupUserInfoDomain = ConvertUserInfoToDomain(signupRequestDto, signupUsersDomain, signupUserRuleTypeDomain);
        signupUserInfoRepository.save(signupUserInfoDomain);

        System.out.println("save");

        System.out.println("success to save the data in tables");

    }
}
