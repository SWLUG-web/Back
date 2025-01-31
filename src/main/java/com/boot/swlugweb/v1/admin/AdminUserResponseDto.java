package com.boot.swlugweb.v1.admin;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUserResponseDto {
    private Integer typeNum;
    private String userId;
    private String nickname;
    private Integer roleType;
    private Long version;

    public AdminUserResponseDto(Integer typeNum, String userId, String nickname, Integer roleType, Long version) {
        this.typeNum = typeNum;
        this.userId = userId;
        this.nickname = nickname;
        this.roleType = roleType;
        this.version = version;
    }
}
