package com.boot.swlugweb.v1.notice;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public NoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    // 게시물 간단 조회
    public List<NoticeDto> getNotices(int page) {
        Pageable pageable = PageRequest.of(page, 5);
        return noticeRepository.findByNoticeDto(pageable);
    }
}
