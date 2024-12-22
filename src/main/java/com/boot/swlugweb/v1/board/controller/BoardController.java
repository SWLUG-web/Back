package com.boot.swlugweb.v1.board.controller;


import com.boot.swlugweb.v1.board.dto.request.BoardCreate2Dto;
import com.boot.swlugweb.v1.board.dto.request.BoardUpdate2Dto;
import com.boot.swlugweb.v1.board.dto.response.BoardView2Dto;
import com.boot.swlugweb.v1.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    @Autowired
    private BoardService boardService;


    @PostMapping("/save")
//    @Operation(summary = "게시글 생성", description = "게시글을 생성합니다.")
    public ResponseEntity<Long> createBoard(@RequestBody BoardCreate2Dto boardCreate2Dto) {
        Long boardId = boardService.createBoard(boardCreate2Dto);
        return ResponseEntity.ok(boardId);
    }

    @PutMapping("/{id}")
//    @Operation(summary = "게시글 수정", description = "게시글을 수정합니다.")
    public ResponseEntity<Void> updateBoard(@RequestBody BoardUpdate2Dto boardUpdate2Dto) {
        boardService.updateBoard(boardUpdate2Dto);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{id}")
//    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id) {
        boardService.deleteBoard(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
//    @Operation(summary = "게시글 조회", description = "게시글을 조회합니다.")
    public ResponseEntity<BoardView2Dto> getBoard(@PathVariable Long id) {
        BoardView2Dto boardView2Dto = boardService.getBoard(id);
        return ResponseEntity.ok(boardView2Dto);
    }

    @GetMapping("/list")
//    @Operation(summary = "게시글 리스트", description = "게시글 리스트를 조회합니다.")
    public ResponseEntity<List<BoardView2Dto>> getBoardList() {
        List<BoardView2Dto> boardList = boardService.getBoardList();
        return ResponseEntity.ok(boardList);
    }




}
