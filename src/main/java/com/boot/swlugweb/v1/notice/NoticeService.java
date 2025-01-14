package com.boot.swlugweb.v1.notice;

import com.boot.swlugweb.v1.blog.BlogDetailRepository;
import com.boot.swlugweb.v1.blog.BlogDomain;
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
        Pageable pageable = PageRequest.of(page, 5);
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
        noticeDomain.setImage(noticeCreateDto.getImages());
        noticeDomain.setIsPin(false);
        noticeDomain.setIsDelete(0);
        noticeDomain.setIsDelete(0);

        return noticeRepository.save(noticeDomain);
    }

}
