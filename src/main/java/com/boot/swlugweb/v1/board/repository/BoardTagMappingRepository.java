package com.boot.swlugweb.v1.board.repository;


import com.boot.swlugweb.v1.board.entity.Board;
import com.boot.swlugweb.v1.board.entity.BoardTag;
import com.boot.swlugweb.v1.board.entity.BoardTagMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardTagMappingRepository extends JpaRepository<BoardTagMapping, Long> {
    void deleteByBoard(Board board);

    @Query("SELECT m FROM BoardTagMapping m WHERE m.board = :board")
    List<BoardTagMapping> findByBoard(@Param("board") Board board);

    List<BoardTagMapping> findByBoardTag(BoardTag boardTag);

}
