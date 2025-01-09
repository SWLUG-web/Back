
package com.boot.swlugweb.v1.notice.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="notice")
@Getter
@Setter
@NoArgsConstructor
public class Notice {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long noticeId;

    private String userId;
    private String title;
    private String content;
    private String imageUrl;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;


}

