package com.boot.swlugweb.v1.board.service;


import com.boot.swlugweb.v1.board.domain.entity.Board;
import com.boot.swlugweb.v1.board.domain.entity.BoardDetail;
import com.boot.swlugweb.v1.board.domain.entity.BoardTag;
import com.boot.swlugweb.v1.board.domain.entity.BoardTagMapping;
import com.boot.swlugweb.v1.board.domain.repository.BoardDetailRepository;
import com.boot.swlugweb.v1.board.domain.repository.BoardRepository;
import com.boot.swlugweb.v1.board.domain.repository.BoardTagMappingRepository;
import com.boot.swlugweb.v1.board.domain.repository.BoardTagRepository;
import com.boot.swlugweb.v1.board.dto.request.BoardCreate2Dto;
import com.boot.swlugweb.v1.board.dto.request.BoardUpdate2Dto;
import com.boot.swlugweb.v1.board.dto.response.BoardView2Dto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardDetailRepository boardDetailRepository;
    private final BoardTagRepository boardTagRepository;
    private final BoardTagMappingRepository boardTagMappingRepository;

    //게시글 생성
    @Transactional
    public Long createBoard(BoardCreate2Dto boardCreate2Dto) {
        // Board 생성
        Board board = new Board();
        board.setUserId(String.valueOf(boardCreate2Dto.getId()));
        board.setBoardCategory(boardCreate2Dto.getCategory());
        board.setBoardTitle(boardCreate2Dto.getTitle());
        board.setCreateAt(boardCreate2Dto.getCreateAt() != null ? boardCreate2Dto.getCreateAt() : LocalDateTime.now());
        board.setUpdateAt(LocalDateTime.now());
        board.setIsPin(false);
        board.setIsSecure(false);
        board.setIsDelete(false);

        Board savedBoard = boardRepository.save(board);

        // BoardDetail 생성
        BoardDetail boardDetail = new BoardDetail();
        boardDetail.setBoard(savedBoard);
        boardDetail.setUserId(String.valueOf(boardCreate2Dto.getId()));
        boardDetail.setBoardContents(boardCreate2Dto.getContents());
        boardDetail.setImage(boardCreate2Dto.getImageUrl());
        boardDetailRepository.save(boardDetail);

        // Tag 처리
        if (boardCreate2Dto.getTag() != null && !boardCreate2Dto.getTag().isEmpty()) {
            for (String tagName : boardCreate2Dto.getTag()) {
                BoardTag tag = boardTagRepository.findByTagName(tagName)
                        .orElseGet(() -> {
                            BoardTag newTag = new BoardTag();
                            newTag.setTagName(tagName);
                            return boardTagRepository.save(newTag);
                        });

                BoardTagMapping mapping = new BoardTagMapping();
                mapping.setBoard(savedBoard);
                mapping.setBoardTag(tag);
                mapping.setUserId(String.valueOf(boardCreate2Dto.getId()));
                boardTagMappingRepository.save(mapping);
            }
        }

        return savedBoard.getBoardId();
    }


    //수정
    @Transactional
    public void updateBoard(BoardUpdate2Dto boardUpdate2Dto) {
        Board board = boardRepository.findById(boardUpdate2Dto.getBoardId().longValue())
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        board.setBoardCategory(boardUpdate2Dto.getCategory());
        board.setBoardTitle(boardUpdate2Dto.getTitle());
        board.setUpdateAt(boardUpdate2Dto.getUpdateAt() != null ? boardUpdate2Dto.getUpdateAt() : LocalDateTime.now());

        // 기존 BoardDetail 수정
        BoardDetail boardDetail = boardDetailRepository.findById(board.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("게시글 상세정보를 찾을 수 없습니다."));
        boardDetail.setBoardContents(boardUpdate2Dto.getContents());
        boardDetail.setImage(boardUpdate2Dto.getImageUrl());
        boardDetailRepository.save(boardDetail);

        // 기존 태그 매핑 삭제
        boardTagMappingRepository.deleteByBoard(board);

        // 새 태그 매핑 생성
        if (boardUpdate2Dto.getTag() != null && !boardUpdate2Dto.getTag().isEmpty()) {
            for (String tagName : boardUpdate2Dto.getTag()) {
                BoardTag tag = boardTagRepository.findByTagName(tagName)
                        .orElseGet(() -> {
                            BoardTag newTag = new BoardTag();
                            newTag.setTagName(tagName);
                            return boardTagRepository.save(newTag);
                        });

                BoardTagMapping mapping = new BoardTagMapping();
                mapping.setBoard(board);
                mapping.setBoardTag(tag);
                mapping.setUserId(String.valueOf(boardUpdate2Dto.getId()));
                boardTagMappingRepository.save(mapping);
            }
        }
    }



    //삭제
    public void deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        board.setIsDelete(true);
        boardRepository.save(board);

    }

    //조회
    @Transactional
    public BoardView2Dto getBoard(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        BoardDetail boardDetail = boardDetailRepository.findById(board.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("게시글 상세정보를 찾을 수 없습니다."));

        List<String> tags = boardTagMappingRepository.findByBoard(board).stream()
                            .map(mapping -> mapping.getBoardTag().getTagName())
                            .collect(Collectors.toList());




        return BoardView2Dto.builder()
                .boardId(board.getBoardId().intValue())
                .category(board.getBoardCategory())
                .title(board.getBoardTitle())
                .tag(tags)
                .roleType(null) // 필요시 추가 로직 구현
                .id(Long.valueOf(board.getUserId()))
                .createAt(board.getCreateAt())
                .updateAt(board.getUpdateAt())
                .contents(boardDetail.getBoardContents())
                .imageUrl(boardDetail.getImage())
                .build();
    }


    //리스트
    @Transactional
    public List<BoardView2Dto> getBoardList() {
        return boardRepository.findAll().stream()
                .filter(board -> !board.getIsDelete())
                .map(board -> {
                    BoardDetail boardDetail = boardDetailRepository.findById(board.getBoardId())
                            .orElseThrow(() -> new IllegalArgumentException("게시글 상세정보를 찾을 수 없습니다."));

                    List<String> tags = boardTagMappingRepository.findByBoard(board).stream()
                            .map(mapping -> mapping.getBoardTag().getTagName())
                            .collect(Collectors.toList());

                    return BoardView2Dto.builder()
                            .boardId(board.getBoardId().intValue())
                            .category(board.getBoardCategory())
                            .title(board.getBoardTitle())
                            .tag(tags)
                            .roleType(null) // 필요시 추가 로직 구현
                            .id(Long.valueOf(board.getUserId()))
                            .createAt(board.getCreateAt())
                            .updateAt(board.getUpdateAt())
                            .contents(boardDetail.getBoardContents())
                            .imageUrl(boardDetail.getImage())
                            .build();
                })
                .collect(Collectors.toList());
    }


}
