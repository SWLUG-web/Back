package com.boot.swlugweb.v1.blog;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BlogUpdateRequestDto {
    private String id;
    private Integer boardCategory;
    private String boardTitle;
    private String boardContent;
    private List<String> tag;
    private List<String> imageUrl;
}
