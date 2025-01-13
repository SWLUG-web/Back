package com.boot.swlugweb.v1.main;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Document(collection = "SwlugWebTest")
@Getter
@Setter
public class MainDomain {

    @Id
    private String id;

    @Field("user_id")
    private String userId;

    @Field("board_title")
    private String boardTitle;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("is_pin")
    private boolean isPin;

    @Field("is_secure")
    private int isSecure;

    @Field("is_delete")
    private int isDelete;
}
