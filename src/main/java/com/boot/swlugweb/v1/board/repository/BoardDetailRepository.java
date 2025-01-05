package com.boot.swlugweb.v1.board.repository;


import com.boot.swlugweb.v1.board.entity.BoardDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardDetailRepository extends JpaRepository<BoardDetail, Long> {}

