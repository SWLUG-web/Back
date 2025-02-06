package com.boot.swlugweb.v1.notice;

import com.boot.swlugweb.v1.blog.GoogleDriveService;
import com.boot.swlugweb.v1.mypage.MyPageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final MyPageRepository myPageRepository;
    private final GoogleDriveService googleDriveService;

    public NoticeService(NoticeRepository noticeRepository, MyPageRepository myPageRepository, GoogleDriveService googleDriveService) {
        this.noticeRepository = noticeRepository;
        this.myPageRepository = myPageRepository;
        this.googleDriveService = googleDriveService;
    }

    public NoticePageResponseDto getNoticesWithPagination(int page, String searchTerm, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<NoticeDto> noticePage;

        long totalNotices = noticeRepository.countByBoardCategoryAndIsDelete(0, 0);

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            noticePage = noticeRepository.findByIsDeleteOrderByIsPinDescCreateAtDesc(0, pageable);
        } else {
            try {
                String decodedSearchTerm = java.net.URLDecoder.decode(searchTerm, "UTF-8");
                String regexPattern = ".*" + decodedSearchTerm.trim()
                        .replaceAll("[\\s]+", " ")
                        .replaceAll(" ", "(?:[ ]|)") + ".*";

                noticePage = noticeRepository.findByBoardTitleContainingAndIsDelete(
                        regexPattern, 0, pageable);
            } catch (Exception e) {
                throw new RuntimeException("검색어 처리 중 오류가 발생했습니다", e);
            }
        }

        // 번호 부여 - 한 번의 쿼리로 처리
        List<NoticeDto> noticesWithNumbers = noticePage.getContent().stream()
                .map(notice -> {
                    String nickname = myPageRepository.findNickname(notice.getUserId());
                    // 현재 게시글보다 최신인 게시글 수를 한 번에 조회
                    long olderCount = noticeRepository.countOlderNotices(0, notice.getCreateAt());
                    notice.setDisplayNumber(totalNotices - olderCount);
                    notice.setNickname(nickname);
                    return notice;
                })
                .collect(Collectors.toList());


        return new NoticePageResponseDto(
                noticesWithNumbers,
                noticePage.getTotalElements(),
                noticePage.getTotalPages(),
                page
        );
    }

    // 공지사항 상세 조회 - 카테고리 체크 추가
    public NoticeDomain getNoticeDetail(String id) {
        NoticeDomain notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + " post not found"));

        // 카테고리가 0이 아니면 예외 발생
        if (notice.getBoardCategory() != 0) {
            throw new IllegalArgumentException("Invalid notice category");
        }

        return notice;
    }

//    // 공지사항 저장
//    public NoticeDomain createNotice(NoticeCreateDto noticeCreateDto, String userId) {
//        NoticeDomain noticeDomain = new NoticeDomain();
//
//        noticeDomain.setUserId(userId);
//        noticeDomain.setBoardCategory(0);
//        noticeDomain.setNoticeTitle(noticeCreateDto.getNoticeTitle());
//        noticeDomain.setNoticeContents(noticeCreateDto.getNoticeContents());
//        noticeDomain.setCreateAt(LocalDateTime.now());
//        noticeDomain.setImage(noticeCreateDto.getImageUrl());
//        noticeDomain.setIsPin(false);
//        noticeDomain.setIsDelete(0);
//
//        return noticeRepository.save(noticeDomain);
//    }

    // google 공지사항 저장
    public NoticeDomain createNotice(NoticeCreateDto noticeCreateDto, String userId) throws GeneralSecurityException, IOException {
        NoticeDomain noticeDomain = new NoticeDomain();

        noticeDomain.setUserId(userId);
        noticeDomain.setBoardCategory(0);
        noticeDomain.setNoticeTitle(noticeCreateDto.getNoticeTitle());
        noticeDomain.setNoticeContents(noticeCreateDto.getNoticeContents());
        noticeDomain.setCreateAt(LocalDateTime.now());
//        noticeDomain.setImage(noticeCreateDto.getImageUrl());
        noticeDomain.setIsPin(false);
        noticeDomain.setIsDelete(0);

        List<String> uploadedImageUrls = new ArrayList<>();
        if (noticeCreateDto.getImageFiles() != null && !noticeCreateDto.getImageFiles().isEmpty()) {
            for (MultipartFile file : noticeCreateDto.getImageFiles()) {
                try {
                    String imageUrl = googleDriveService.uploadFile(file);
                    uploadedImageUrls.add(imageUrl);
                } catch (Exception e) {
                    System.err.println("이미지 업로드 실패: " + file.getOriginalFilename());
                    e.printStackTrace();
                }
            }
        }
        noticeDomain.setImage(uploadedImageUrls);

        return noticeRepository.save(noticeDomain);
    }


//    // 공지사항 수정
//    public void updateNotice(NoticeUpdateRequestDto noticeUpdateRequestDto, String userId) {
//        // 기존 데이터 조회
//        String id = noticeUpdateRequestDto.getId();
//
//        NoticeDomain notice = noticeRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException(id + "게시물이 없습니다."));
//
//        // 필드 업데이트
//        if (noticeUpdateRequestDto.getNoticeTitle() != null) {
//            notice.setNoticeTitle(noticeUpdateRequestDto.getNoticeTitle());
//        }
//        if (noticeUpdateRequestDto.getNoticeContents() != null) {
//            notice.setNoticeContents(noticeUpdateRequestDto.getNoticeContents());
//        }
//        if (noticeUpdateRequestDto.getImageUrl() != null) {
//            notice.setImage(noticeUpdateRequestDto.getImageUrl());
//        }
//
//        noticeRepository.save(notice);
//    }

//    // google 공지사항 수정 (최종x)
//    public void updateNotice(NoticeUpdateRequestDto noticeUpdateRequestDto, String userId) throws GeneralSecurityException, IOException {
//        // 기존 데이터 조회
//        String id = noticeUpdateRequestDto.getId();
//
//        NoticeDomain notice = noticeRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException(id + "게시물이 없습니다."));
//
//        // 필드 업데이트
//        if (noticeUpdateRequestDto.getNoticeTitle() != null) {
//            notice.setNoticeTitle(noticeUpdateRequestDto.getNoticeTitle());
//        }
//        if (noticeUpdateRequestDto.getNoticeContents() != null) {
//            notice.setNoticeContents(noticeUpdateRequestDto.getNoticeContents());
//        }
////        if (noticeUpdateRequestDto.getImageUrl() != null) {
////            notice.setImage(noticeUpdateRequestDto.getImageUrl());
////        }
//
//        List<String> currentImageUrls = notice.getImage() != null ? new ArrayList<>(notice.getImage()) : new ArrayList<>();
//        List<String> updatedImageUrls = noticeUpdateRequestDto.getImageUrl() != null ? noticeUpdateRequestDto.getImageUrl() : new ArrayList<>();
//        List<String> imagesToDelete = new ArrayList<>(currentImageUrls);
//        imagesToDelete.removeAll(updatedImageUrls);
//
//        for (String imageUrl : imagesToDelete) {
//            String fileId = extractFileIdFromUrl(imageUrl);
//            try {
//                googleDriveService.deleteFile(fileId);
//            } catch (Exception e) {
//                System.err.println("이미지 삭제 실패: " + imageUrl);
//                e.printStackTrace();
//            }
//        }
//
//        if (noticeUpdateRequestDto.getImageFiles() != null) {
//            for (MultipartFile file : noticeUpdateRequestDto.getImageFiles()) {
//                String imageUrl = googleDriveService.uploadFile(file);
//                updatedImageUrls.add(imageUrl);
//            }
//        }
//
//        notice.setImage(updatedImageUrls);
//
//        noticeRepository.save(notice);
//    }


//  google 수정 (최종)
    public void updateNotice(NoticeUpdateRequestDto noticeUpdateRequestDto, String userId) throws GeneralSecurityException, IOException {
        // 기존 데이터 조회
        String id = noticeUpdateRequestDto.getId();
        NoticeDomain notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + " 게시물이 없습니다."));

        // 필드 업데이트 (글 제목과 내용)
        if (noticeUpdateRequestDto.getNoticeTitle() != null) {
            notice.setNoticeTitle(noticeUpdateRequestDto.getNoticeTitle());
        }
        if (noticeUpdateRequestDto.getNoticeContents() != null) {
            notice.setNoticeContents(noticeUpdateRequestDto.getNoticeContents());
        }

        // 기존 이미지 목록 가져오기
        List<String> currentImageUrls = notice.getImage() != null ? new ArrayList<>(notice.getImage()) : new ArrayList<>();

        // 이미지 삭제 로직 (요청에 포함되지 않은 이미지만 삭제)
        if (noticeUpdateRequestDto.getImageUrl() != null) {
            List<String> updatedImageUrls = new ArrayList<>(noticeUpdateRequestDto.getImageUrl());

            // 삭제할 이미지 추출
            List<String> imagesToDelete = new ArrayList<>(currentImageUrls);
            imagesToDelete.removeAll(updatedImageUrls);

            // 삭제 처리
            for (String imageUrl : imagesToDelete) {
                String fileId = extractFileIdFromUrl(imageUrl);
                try {
                    googleDriveService.deleteFile(fileId);
                    currentImageUrls.remove(imageUrl);
                } catch (Exception e) {
                    System.err.println("이미지 삭제 실패: " + imageUrl);
                    e.printStackTrace();
                }
            }

            // 요청 이미지가 반영된 목록으로 업데이트
            currentImageUrls = updatedImageUrls;
        }

        // 이미지 파일 추가 처리
        if (noticeUpdateRequestDto.getImageFiles() != null) {
            for (MultipartFile file : noticeUpdateRequestDto.getImageFiles()) {
                String imageUrl = googleDriveService.uploadFile(file); // 구글 드라이브에 이미지 업로드
                currentImageUrls.add(imageUrl); // 업로드된 이미지 추가
            }
        }

        // 최종 이미지 정보 설정
        notice.setImage(currentImageUrls);

        // 데이터 저장
        noticeRepository.save(notice);
    }






    private String extractFileIdFromUrl(String imageUrl) {
        String prefix = "https://drive.google.com/uc?id=";
        if (imageUrl != null && imageUrl.startsWith(prefix)) {
            return imageUrl.substring(prefix.length());
        }
        return null;
    }


//    // notice 삭제
//    public void deleteNotice(String id, String userId) {
//        NoticeDomain notice = noticeRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException(id + "게시물이 없습니다."));
//
//        noticeRepository.deleteById(id);
//    }

    // google notice 삭제
//    public void deleteNotice(String id, String userId) throws GeneralSecurityException, IOException {
//        NoticeDomain notice = noticeRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException(id + "게시물이 없습니다."));
//
//        List<String> imageUrls = notice.getImage();
//        if (imageUrls != null && !imageUrls.isEmpty()) {
//            for (String imageUrl : imageUrls) {
//                String fileId = extractFileIdFromUrl(imageUrl);
//                if (fileId != null) {
//                    googleDriveService.deleteFile(fileId);
//                }
//            }
//        }
//
//        noticeRepository.deleteById(id);
//    }

    // google notice 삭제 만약 이미 사진이 삭제된 경우
    public void deleteNotice(String id, String userId) throws GeneralSecurityException, IOException {
        // 게시글 조회
        NoticeDomain notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + " 게시물이 없습니다."));

        // 이미지 URL 목록 가져오기
        List<String> imageUrls = notice.getImage();
        if (imageUrls != null && !imageUrls.isEmpty()) {
            for (String imageUrl : imageUrls) {
                String fileId = extractFileIdFromUrl(imageUrl);
                if (fileId != null) {
                    try {
                        // 이미지 파일 삭제 시 예외 처리
                        googleDriveService.deleteFile(fileId);
                    } catch (Exception e) {
                        // 파일 삭제 실패 시, 오류 로그 출력 후 계속 진행
                        System.err.println("이미지 삭제 실패: " + imageUrl);
                        e.printStackTrace();
                    }
                }
            }
        }

        // 게시글 삭제
        noticeRepository.deleteById(id);
    }


    public Map<String, NoticeSummaryDto> getAdjacentNotices(String id) {
        Map<String, NoticeSummaryDto> result = new HashMap<>();

        NoticeDomain currentNotice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + " 게시물이 없습니다."));
        LocalDateTime currentCreateAt = currentNotice.getCreateAt();

        // 이전글 조회 (더 최신 글 중 첫번째)
        List<NoticeDomain> prevNotices = noticeRepository.findPrevNotices(currentCreateAt);
        if (!prevNotices.isEmpty()) {
            NoticeDomain prevNotice = prevNotices.stream()
                    .min((a, b) -> a.getCreateAt().compareTo(b.getCreateAt()))
                    .get();
            NoticeSummaryDto prevDto = new NoticeSummaryDto();
            prevDto.setId(prevNotice.getId());
            prevDto.setNoticeTitle(prevNotice.getNoticeTitle());
            result.put("previous", prevDto);
        }

        // 다음글 조회 (더 오래된 글 중 첫번째)
        List<NoticeDomain> nextNotices = noticeRepository.findNextNotices(currentCreateAt);
        if (!nextNotices.isEmpty()) {
            NoticeDomain nextNotice = nextNotices.stream()
                    .max((a, b) -> a.getCreateAt().compareTo(b.getCreateAt()))
                    .get();
            NoticeSummaryDto nextDto = new NoticeSummaryDto();
            nextDto.setId(nextNotice.getId());
            nextDto.setNoticeTitle(nextNotice.getNoticeTitle());
            result.put("next", nextDto);
        }

        return result;
    }
}

