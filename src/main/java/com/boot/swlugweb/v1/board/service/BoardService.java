package com.boot.swlugweb.v1.board.service;

import com.boot.swlugweb.v1.board.entity.Board;
import com.boot.swlugweb.v1.board.entity.BoardDetail;
import com.boot.swlugweb.v1.board.entity.BoardTag;
import com.boot.swlugweb.v1.board.entity.BoardTagMapping;
import com.boot.swlugweb.v1.board.repository.BoardDetailRepository;
import com.boot.swlugweb.v1.board.repository.BoardRepository;
import com.boot.swlugweb.v1.board.repository.BoardTagMappingRepository;
import com.boot.swlugweb.v1.board.repository.BoardTagRepository;
import com.boot.swlugweb.v1.board.dto.request.BoardCreate2Dto;
import com.boot.swlugweb.v1.board.dto.request.BoardUpdate2Dto;
import com.boot.swlugweb.v1.board.dto.response.BoardView2Dto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
//@RequiredArgsConstructor
public class BoardService {

//    private final BoardRepository boardRepository;
//    private final BoardDetailRepository boardDetailRepository;
//    private final BoardTagRepository boardTagRepository;
//    private final BoardTagMappingRepository boardTagMappingRepository;
//
//    public BoardService(BoardRepository boardRepository, BoardDetailRepository boardDetailRepository, BoardTagRepository boardTagRepository, BoardTagMappingRepository boardTagMappingRepository) {
//        this.boardRepository = boardRepository;
//        this.boardDetailRepository = boardDetailRepository;
//        this.boardTagRepository = boardTagRepository;
//        this.boardTagMappingRepository = boardTagMappingRepository;
//    }
    @Autowired(required = true)
    private BoardRepository boardRepository;
    @Autowired(required = true)
    private BoardDetailRepository boardDetailRepository;
    @Autowired(required = true)
    private BoardTagRepository boardTagRepository;
    @Autowired(required = true)
    private BoardTagMappingRepository boardTagMappingRepository;


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
                .tag(tags.toString())
//                .roleType(null)
                .id(Long.valueOf(board.getUserId()))
                .createAt(board.getCreateAt())
                .updateAt(board.getUpdateAt())
                .contents(boardDetail.getBoardContents())
                .imagePath(boardDetail.getImage())
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
                            .tag(tags.toString())
//                            .roleType(null)
                            .id(Long.valueOf(board.getUserId()))
                            .createAt(board.getCreateAt())
                            .updateAt(board.getUpdateAt())
                            .contents(boardDetail.getBoardContents())
                            .imagePath(boardDetail.getImage())
                            .build();
                })
                .collect(Collectors.toList());
    }


    // 검색 기능 추가
    @Transactional
    public List<BoardView2Dto> searchBoards(String keyword) {
        return boardRepository.findAll().stream()
                .filter(board -> !board.getIsDelete()) // 삭제된 게시글 제외
                .filter(board -> {
                    BoardDetail boardDetail = boardDetailRepository.findById(board.getBoardId())
                            .orElse(null);
                    if (boardDetail == null) return false;

                    String title = board.getBoardTitle();
                    String content = boardDetail.getBoardContents();
                    return (title != null && title.contains(keyword)) ||
                            (content != null && content.contains(keyword));
                })
                .map(board -> {
                    BoardDetail boardDetail = boardDetailRepository.findById(board.getBoardId())
                            .orElseThrow(() -> new IllegalArgumentException("게시글 상세정보를 찾을 수 없습니다."));

                    List<String> tags = boardTagMappingRepository.findByBoard(board).stream()
                            .map(BoardTagMapping::getBoardTag)
                            .map(tag -> tag.getTagName())
                            .collect(Collectors.toList());

                    return BoardView2Dto.builder()
                            .boardId(board.getBoardId().intValue())
                            .category(board.getBoardCategory())
                            .title(board.getBoardTitle())
                            .tag(tags.toString())
//                            .roleType(null)
                            .id(Long.valueOf(board.getUserId()))
                            .createAt(board.getCreateAt())
                            .updateAt(board.getUpdateAt())
                            .contents(boardDetail.getBoardContents())
                            .imagePath(boardDetail.getImage())
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public List<String> getAllTags() {
        return boardTagRepository.findAll().stream()
                .map(BoardTag::getTagName)
                .collect(Collectors.toList());
    }
    // 태그로 게시글 필터링
    @Transactional
    public List<BoardView2Dto> getBoardListByTag(String tagName) {
        Optional<BoardTag> tag = boardTagRepository.findByTagName(tagName);

        if (tag.isEmpty()) {
            return List.of(); // 태그가 없으면 빈 리스트 반환
        }

        // 주어진 태그에 해당하는 모든 매핑을 찾기
        return boardTagMappingRepository.findByBoardTag(tag.get()).stream()
                .filter(mapping -> !mapping.getBoard().getIsDelete()) // 삭제된 게시글 제외
                .map(mapping -> {
                    Board board = mapping.getBoard();  // getBoard() 메서드를 통해 Board 객체 접근
                    BoardDetail boardDetail = boardDetailRepository.findById(board.getBoardId())
                            .orElseThrow(() -> new IllegalArgumentException("게시글 상세정보를 찾을 수 없습니다."));

                    // 게시글에 매핑된 태그 목록 가져오기
                    List<String> tagsForBoard = boardTagMappingRepository.findByBoard(board).stream()
                            .map(mappingForBoard -> mappingForBoard.getBoardTag().getTagName())
                            .collect(Collectors.toList());

                    return BoardView2Dto.builder()
                            .boardId(board.getBoardId().intValue())
                            .category(board.getBoardCategory())
                            .title(board.getBoardTitle())
                            .tag(tagsForBoard.toString())  // 게시글에 관련된 태그들
                            .id(Long.valueOf(board.getUserId()))
                            .createAt(board.getCreateAt())
                            .updateAt(board.getUpdateAt())
                            .contents(boardDetail.getBoardContents())
                            .imagePath(boardDetail.getImage())
                            .build();
                })
                .collect(Collectors.toList());
    }

    public List<BoardView2Dto> getBoardsByCategory(Integer category){
        List<Board> boards = boardRepository.findByBoardCategory(category);
        return boards.stream()
                .map(BoardView2Dto::new)
                .collect(Collectors.toList());
    }






}
