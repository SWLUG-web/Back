package com.boot.swlugweb.v1.admin;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users")
public class AdminUsersDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "users_num")
    private Integer usersNum;

    @Version
    private Integer version;

    private String userId;
    private String pw;
}
