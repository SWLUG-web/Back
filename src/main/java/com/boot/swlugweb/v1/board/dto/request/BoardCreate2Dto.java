package com.boot.swlugweb.v1.board.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
//@Schema(description="블로그 입력 객체")
public class BoardCreate2Dto {
//    @Schema(description = "블로그 카테고리", example = "후기")
    private Integer category;

//    @Schema(description = "블로그 제목", example = "팀플 활동 후기")
    private String title;

//    @Schema(description = "블로그 태그", example = "#후기")
    private List<String> tag;

//    @Schema(description = "사용자 분류", example = "ADMIN")
    private String roleType;

//    @Schema(description = "작성자 id", example = "abc123")
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;

//    @Schema(description = "블로그 내용", example = "팀플 활동")
    private String contents;

//    @Schema(description = "블로그 사진")
    private String imageUrl;
}
