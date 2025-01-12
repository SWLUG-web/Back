package com.boot.swlugweb.v1.blog;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Builder
public class BlogUpdateDto {
    private Integer boardCategory;
    private String boardTitle;
    private String boardContent;
    private List<String> tag;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createAt;
    private List<String> imageUrl;
}
