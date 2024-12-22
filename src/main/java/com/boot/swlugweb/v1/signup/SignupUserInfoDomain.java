package com.boot.swlugweb.v1.signup;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "user_info")
public class SignupUserInfoDomain {
    //user_info 테이블에 들어갈 객체 정의

    @Id
    @Column(name = "user_id")
    private String user_id;

    @Version
    private Long version;

    private String email;
    private String phone;

    @OneToOne(mappedBy = "signupUserInfo", cascade = CascadeType.ALL)
    private SignupUsersDomain signupUsers;

    @OneToOne(mappedBy = "signupUserInfoDomain", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private SignupUserRuleTypeDomain signupUserRuleType;

    //생성자 선언
    public SignupUserInfoDomain() {}

    // 연관관계 편의 메서드 (선택 사항)
//    public void setSignupUsers(SignupUsersDomain signupUsers) {
//        this.signupUsers = signupUsers;
//    }
//
//    public void setSignupUserRuleType(SignupUserRuleTypeDomain signupUserRuleType) {
//        this.signupUserRuleType = signupUserRuleType;
//    }
}
