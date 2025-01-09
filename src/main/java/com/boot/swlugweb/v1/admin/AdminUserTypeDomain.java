package com.boot.swlugweb.v1.admin;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "user_type")
public class AdminUserTypeDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_num")
    private Integer typeNum;

    @Version
    private Integer version;

    private String nickname;
    @Column(name = "role_type")
    private Integer roleType;
    private String userId;
}
