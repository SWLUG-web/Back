package com.boot.swlugweb.v1.notice;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class NoticeDto {

    @Id
    private String id;

    @Field("user_id")
    private String userId;

    @Field("board_title")
    private String boardTitle;

    @Field("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createAt;

    @Field("tag")
    private List<String> tag;

    @Field("is_pin")
    private Boolean isPin = false;

    @Field("is_secure")
    private Integer isSecure = 0;

    @Field("is_delete")
    private Integer isDelete = 0;

    // 추가된 필드
    private Long displayNumber;
}