package com.boot.swlugweb.v1.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AdminUserResponseDto {
    private String userId;
    private String email;
    private String phone;
    private String nickname;
    private Integer roleType;

}
