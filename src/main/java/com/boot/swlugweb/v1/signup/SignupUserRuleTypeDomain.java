package com.boot.swlugweb.v1.signup;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="user_type")
public class SignupUserRuleTypeDomain {

    @Id
    @Column(name = "user_id")
    private String user_id;

    @Version
    private Long version;

    private String nickname;
    private Integer ruleType;

    @OneToOne
    @JoinColumn(name = "user_id") //객체를 여러 번 저장하는 일을 피하고자..
    private SignupUserInfoDomain signupUserInfoDomain;

}
