package com.boot.swlugweb.v1.board.domain.repository;


import com.boot.swlugweb.v1.board.domain.entity.BoardTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardTagRepository extends JpaRepository<BoardTag, Long> {
    Optional<BoardTag> findByTagName(String TagName);
}
