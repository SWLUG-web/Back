package com.boot.swlugweb.v1.board.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="board")
@Getter
@Setter
@NoArgsConstructor
public class Board {

    @Id
    @Column(name="board_id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long boardId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="user_id",nullable=false)
//    private User user;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name="board_category",nullable = false)
    private Integer boardCategory;

    @Column(name="board_title",nullable = false)
    private String boardTitle;

    @Column(name = "created_at", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") // JSON 직렬화 시 포맷 지정
    private LocalDateTime createAt;


    @Column(name="updated_at", nullable = false)
    private LocalDateTime updateAt;


    @Column(name="is_pin", nullable = false)
    private Boolean isPin = false;

    @Column(name="is_secure", nullable = false)
    private Boolean isSecure = false;

    @Column(name="is_delete", nullable = false)
    private Boolean isDelete = false;

    @OneToOne(mappedBy="board",cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    private BoardDetail boardDetail;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardTagMapping> boardTagMappings = new ArrayList<>();



}
