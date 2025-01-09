package com.boot.swlugweb.v1.board.controller;


import com.boot.swlugweb.v1.board.dto.request.BoardCreate2Dto;
import com.boot.swlugweb.v1.board.dto.request.BoardUpdate2Dto;
import com.boot.swlugweb.v1.board.dto.response.BoardView2Dto;
import com.boot.swlugweb.v1.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    @Autowired
    private BoardService boardService;


//    @Autowired
//    public BoardController(BoardService boardService) {
//        this.boardService = boardService;
//    }

//    private final BoardService boardService;
//    public BoardController(BoardService boardService) {
//        this.boardService = boardService;
//    }


    @PostMapping("/save")
    //@Operation(summary = "게시글 생성", description = "게시글을 생성합니다.")
    public RedirectView createBoard(@RequestBody BoardCreate2Dto boardCreate2Dto) {
        boardService.createBoard(boardCreate2Dto);
        return new RedirectView("/api/board/blog");
    }

    @PutMapping("/write/{id}")
    //@Operation(summary = "게시글 수정", description = "게시글을 수정합니다.")
    public RedirectView updateBoard(@RequestBody BoardUpdate2Dto boardUpdate2Dto) {
        boardService.updateBoard(boardUpdate2Dto);
        return new RedirectView("/api/board/blog");
    }


    @DeleteMapping("/delete/{id}")
    //@Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
    public RedirectView deleteBoard(@PathVariable Long id) {
        boardService.deleteBoard(id);
        return new RedirectView("/api/board/blog");
    }
    //    @DeleteMapping("/delete/{id}")
//    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
//    public ResponseEntity<List<BoardView2Dto>> deleteBoard(@PathVariable Long id) {
//    // 로그인한 사용자 정보 가져오기
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String currentRoleType = userDetails.getAuthorities().stream()
//            .map(authority -> authority.getAuthority())
//            .findFirst().orElse("");
//
//    // 게시글 삭제 서비스 호출
//    boardService.deleteBoard(id, currentRoleType, Long.valueOf(userDetails.getUsername()));
//
//    // 삭제 후 게시글 목록 가져오기
//    List<BoardView2Dto> boardList = boardService.getBoardList();
//    return ResponseEntity.ok(boardList);
//}
    @GetMapping("/view/{id}")
    //@Operation(summary = "게시글 조회", description = "게시글을 조회합니다.")
    public ResponseEntity<BoardView2Dto> getBoard(@PathVariable Long id) {
        BoardView2Dto boardView2Dto = boardService.getBoard(id);
        return ResponseEntity.ok(boardView2Dto);
    }

    @GetMapping("/blog")
    //@Operation(summary = "게시글 리스트", description = "게시글 리스트를 조회합니다.")
    public ResponseEntity<List<BoardView2Dto>> getBoardList() {
        List<BoardView2Dto> boardList = boardService.getBoardList();
        return ResponseEntity.ok(boardList);
    }

    // 게시글 검색
    @GetMapping("/search")
    public ResponseEntity<List<BoardView2Dto>> searchBoards(@RequestParam String keyword) {
        List<BoardView2Dto> boards = boardService.searchBoards(keyword);
        return ResponseEntity.ok(boards);
    }

    //태그별 게시글 조회
    @GetMapping("/listByTag")
    public List<BoardView2Dto> getBoardListByTag(@RequestParam String tag){
        return boardService.getBoardListByTag(tag);
    }

    //카테고리 선택
    @GetMapping("/category/{category}")
    public ResponseEntity<List<BoardView2Dto>> getBoardsByCategory(@PathVariable Integer category) {
        List<BoardView2Dto> boards = boardService.getBoardsByCategory(category);
        return ResponseEntity.ok(boards);
    }





}
