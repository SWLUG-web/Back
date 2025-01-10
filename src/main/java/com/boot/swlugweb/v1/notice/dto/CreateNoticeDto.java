
package com.boot.swlugweb.v1.notice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CreateNoticeDto {
    private Integer noticeId;
    private String title;
    private String content;
    private String imagePath;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createAt;
    private String roleType;
    private Long id;
}
