package com.boot.swlugweb.v1.blog;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BlogDto {
    private String id;
    private Integer boardCategory;
    private String boardTitle;
    private LocalDateTime createAt;
    private String userId;
}
