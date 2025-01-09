package com.boot.swlugweb.v1.board.dto.response;

import com.boot.swlugweb.v1.board.entity.Board;
import com.fasterxml.jackson.annotation.JsonFormat;

//import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@Schema(description="블로그 조회 객체")
public class BoardView2Dto {

    //@Schema(description = "블로그 아이디", example = "1")
    private Integer boardId;

    //@Schema(description = "블로그 카테고리", example = "후기")
    private Integer category;

    //@Schema(description = "블로그 제목", example = "팀플 활동 후기")
    private String title;

    //@Schema(description = "블로그 태그", example = "#후기")
    private String tag;

    //@Schema(description = "사용자 분류", example = "ADMIN")
    private String roleType;

    //@Schema(description = "작성자 id", example = "abc123")
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateAt;

    
    //@Schema(description = "블로그 내용", example = "팀플 활동")
    private String contents;

    //@Schema(description = "블로그 사진")
    private String imageUrl;

    public BoardView2Dto(Board board) {
        this.boardId = Math.toIntExact(board.getBoardId());
        this.id = Long.valueOf(board.getUserId());
        this.category = board.getBoardCategory();
        this.title = board.getBoardTitle();
        this.createAt = board.getCreateAt();
        this.updateAt = board.getUpdateAt();

        if (board.getBoardDetail() != null) {
            this.contents = board.getBoardDetail().getBoardContents();
            this.imageUrl = board.getBoardDetail().getImage();
        }

    }
}
