package com.boot.swlugweb.v1.notice;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NoticeUpdateRequestDto {
    private String id;
    private String noticeTitle;
    private String noticeContents;
    private List<String> imageUrl;
}
