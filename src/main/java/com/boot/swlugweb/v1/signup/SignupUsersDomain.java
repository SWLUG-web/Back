package com.boot.swlugweb.v1.signup;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name ="users")
public class SignupUsersDomain {
    //users 테이블에 들어갈 객체 정의
    @Id
    @Column(name = "user_id")
    private String user_id;

    @Version
    private Long version;

    private String pw;

    //test db에 맞춤
    private Integer id;
    private Integer account_locked;
    private String password;
    private Integer try_count;
    private String userId;



    @OneToOne
    @JoinColumn(name = "user_id")
    private SignupUserInfoDomain signupUserInfo; //users_info 테이블을 참고함을 명시
}
