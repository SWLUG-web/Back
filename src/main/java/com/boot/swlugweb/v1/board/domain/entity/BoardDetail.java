package com.boot.swlugweb.v1.board.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="board_detatil")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardDetail {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="board_id")
    private Long boardId;

    @Column(name="user_id", nullable=false)
    private String userId;

    @Column(name = "board_contents", columnDefinition = "TEXT")
    private String boardContents;

    private String image;

    private String imagePath;

    @OneToOne
    @JoinColumn(name="board_id", referencedColumnName = "board_id")
    private Board board;

}
