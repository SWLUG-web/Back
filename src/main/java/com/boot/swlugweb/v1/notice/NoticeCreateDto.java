package com.boot.swlugweb.v1.notice;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Builder
public class NoticeCreateDto {
    private String noticeTitle;
    private String noticeContents;

    private List<String> images;
}
