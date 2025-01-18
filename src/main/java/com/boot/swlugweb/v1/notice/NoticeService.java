package com.boot.swlugweb.v1.notice;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public NoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    // 공지사항 간단 조회
    public List<NoticeDto> getNotices(int page) {
        Pageable pageable = PageRequest.of(page, 10);

        return noticeRepository.findByNoticeDto(pageable);
    }

    // 공지사항 상세 조회
    public NoticeDomain getNoticeDetail(String id) {
        return noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + "post not found"));
    }

    // 공지사항 저장
    public NoticeDomain createNotice(NoticeCreateDto noticeCreateDto, String userId) {
        NoticeDomain noticeDomain = new NoticeDomain();

        noticeDomain.setUserId(userId);
        noticeDomain.setBoardCategory(0);
        noticeDomain.setNoticeTitle(noticeCreateDto.getNoticeTitle());
        noticeDomain.setNoticeContents(noticeCreateDto.getNoticeContents());
        noticeDomain.setCreateAt(LocalDateTime.now());
        noticeDomain.setImage(noticeCreateDto.getImageUrl());
        noticeDomain.setIsPin(false);
        noticeDomain.setIsDelete(0);
        noticeDomain.setIsDelete(0);

        return noticeRepository.save(noticeDomain);
    }

    // 공지사항 수정
    public void updateNotice(NoticeUpdateRequestDto noticeUpdateRequestDto, String userId) {

        // 기존 데이터 조회
        String id = noticeUpdateRequestDto.getId();

        NoticeDomain notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + "게시물이 없습니다."));

        // 필드 업데이트
        if (noticeUpdateRequestDto.getNoticeTitle() != null) {
            notice.setNoticeTitle(noticeUpdateRequestDto.getNoticeTitle());
        }
        if (noticeUpdateRequestDto.getNoticeContents() != null) {
            notice.setNoticeContents(noticeUpdateRequestDto.getNoticeContents());
        }
        if (noticeUpdateRequestDto.getImageUrl() != null) {
            notice.setImage(noticeUpdateRequestDto.getImageUrl());
        }
        notice.setCreateAt(LocalDateTime.now());

        noticeRepository.save(notice);
    }

    // notice 삭제
    public void deleteNotice(String id, String userId) {
        NoticeDomain notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + "게시물이 없습니다."));

        noticeRepository.deleteById(id);
    }
}
