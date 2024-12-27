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
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto_increment 설정
    @Column(name = "user_info_num")
    private Integer userInfoNum;

    @Version
    private Long version;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    // FK를 주테이블이 갖는 일대일 단방향 관계에서는 대상 테이블에 해당하는 클래스를 참조 필드로 작성
    // name에 외래키 이름 작성
    @OneToOne()
    @JoinColumn(name = "users_num")
    private SignupUsersDomain signupUsers;

    @OneToOne()
    @JoinColumn(name = "type_num")
    private SignupUserRuleTypeDomain signupUserRuleType;

}
