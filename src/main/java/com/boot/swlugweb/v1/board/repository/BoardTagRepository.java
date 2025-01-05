package com.boot.swlugweb.v1.board.repository;


import com.boot.swlugweb.v1.board.entity.BoardTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardTagRepository extends JpaRepository<BoardTag, Long> {
    Optional<BoardTag> findByTagName(String TagName);
}
