package com.boot.swlugweb.v1.blog;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

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
    private List<String> imageUrls; //유지하려는 이미지 URL 목록
    private List<MultipartFile> imageFiles; // 새로 업로드할 이미지 파일 목록
}
