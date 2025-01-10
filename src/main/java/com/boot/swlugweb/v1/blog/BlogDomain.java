package com.boot.swlugweb.v1.blog;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "SwlugWeb")
@Getter
@Setter
public class BlogDomain {

    @Id
    private String id;

    @Field("user_id")
    private String userId;

    @Field("board_category")
    private Integer boardCategory;

    @Field("board_title")
    private String boardTitle;

    @Field("board_contents")
    private String boardContents;

    @Field("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // JSON 직렬화 시 포맷 지정
    private LocalDateTime createAt;

    @Field("is_pin")
    private Boolean isPin = false;

    @Field("is_secure")
    private Boolean isSecure = false;

    @Field("is_delete")
    private Boolean isDelete = false;

}
