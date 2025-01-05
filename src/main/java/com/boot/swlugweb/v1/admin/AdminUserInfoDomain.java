package com.boot.swlugweb.v1.admin;

import com.boot.swlugweb.v1.signup.SignupUserRuleTypeDomain;
import com.boot.swlugweb.v1.signup.SignupUsersDomain;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="user_info")
public class AdminUserInfoDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "users_num")
    private Integer usersInfoNum;

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
    private AdminUsersDomain usersNum;

    @OneToOne()
    @JoinColumn(name = "type_num")
    private AdminUserTypeDomain userType;

}
