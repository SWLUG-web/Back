package com.boot.swlugweb.v1.board.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BoardTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    @Column(name = "tag_name", nullable = false, unique = true)
    private String tagName;

    @OneToMany(mappedBy = "boardTag", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardTagMapping> boardTagMappings = new ArrayList<>();

    @Builder
    public BoardTag(String tagName) {
        this.tagName = tagName;
    }

}
