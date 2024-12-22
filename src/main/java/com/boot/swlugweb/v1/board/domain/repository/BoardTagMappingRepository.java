package com.boot.swlugweb.v1.board.domain.repository;


import com.boot.swlugweb.v1.board.domain.entity.Board;
import com.boot.swlugweb.v1.board.domain.entity.BoardTagMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardTagMappingRepository extends JpaRepository<BoardTagMapping, Long> {
    void deleteByBoard(Board board);

    @Query("SELECT m FROM BoardTagMapping m WHERE m.board = :board")
    List<BoardTagMapping> findByBoard(@Param("board") Board board);

}
