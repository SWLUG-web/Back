package com.boot.swlugweb.v1.blog;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BlogDto {
    private String id;
    private Integer boardCategory;
    private String boardTitle;
    private LocalDateTime createAt;
    private String userId;

    private List<String> tag;
    private List<String> image;//추가
//    private List<String> imageUrl; // image로 변경

    private Boolean isPin = false;
    private Integer isSecure = 0;
    private Integer isDelete = 0;

    // 추가된 필드
    private Long displayNumber;
}
