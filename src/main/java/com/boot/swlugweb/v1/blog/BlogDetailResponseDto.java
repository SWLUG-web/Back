package com.boot.swlugweb.v1.blog;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlogDetailResponseDto {
    private BlogDomain blogs;
    private String nickname;
}
