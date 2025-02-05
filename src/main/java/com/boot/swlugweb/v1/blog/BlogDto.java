package com.boot.swlugweb.v1.blog;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BlogDto {
    private String id;
    private Integer boardCategory;
    private String boardTitle;
    private LocalDateTime createAt;
    private String userId;
    private String nickname;
    private String categoryName;

    private List<String> tag;
    private List<String> image;

    private Boolean isPin = false;
    private Integer isSecure = 0;
    private Integer isDelete = 0;

    private String thumbnailImage;

    public String getThumbnailUrl() {
        // 썸네일 이미지가 null이거나 정의 x 않을 때
        if (thumbnailImage == null || thumbnailImage.trim().isEmpty()) {
            // 이미지가 있다면 첫 번째 이미지 사용
            if (image != null && !image.isEmpty()) {
                return image.get(0);
            }
            // 이미지도 없으면 null 반환
            return null;
        }
        // 썸네일 이미지가 있으면 해당 이미지 반환
        return thumbnailImage;
    }


}
