
package com.boot.swlugweb.v1.notice.controller;


import com.boot.swlugweb.v1.notice.dto.CreateNoticeDto;
import com.boot.swlugweb.v1.notice.dto.UpdateNoticeDto;
import com.boot.swlugweb.v1.notice.dto.ViewNoticeDto;
import com.boot.swlugweb.v1.notice.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    // 공지글 등록
    @PostMapping("/save")
    public RedirectView createNotice(@RequestPart(value="createNoticeDto",required=false) CreateNoticeDto createNoticeDto,@RequestPart(value="noticeImg", required=false) MultipartFile noticeImg) throws IOException {
       noticeService.createNotice(createNoticeDto, noticeImg);
        return new RedirectView("/api/notice/list");
    }

    // 공지글 수정
    @PutMapping("/write/{NoticeId}")
    public RedirectView updateNotice(@PathVariable("NoticeId") Long noticeId, @RequestPart(value="updateNoticeDto",required=false) UpdateNoticeDto updateNoticeDto,@RequestPart(value="noticeImg", required=false) MultipartFile noticeImg) throws IOException {
        noticeService.updateNotice(noticeId,updateNoticeDto, noticeImg);
        return new RedirectView("/api/notice/list");  // 수정 후 리스트 페이지로 리다이렉트
    }

    // 공지글 삭제
    @DeleteMapping("/delete/{noticeId}")
    public RedirectView deleteNotice(@PathVariable Long noticeId) {
        noticeService.deleteNotice(noticeId);
        return new RedirectView("/api/notice/list");  // 삭제 후 리스트 페이지로 리다이렉트
    }

    // 공지글 조회
    @GetMapping("/{noticeId}")
    public ResponseEntity<ViewNoticeDto> getNotice(@PathVariable Long noticeId) {
        ViewNoticeDto notice = noticeService.getNotice(noticeId);
        return ResponseEntity.ok(notice);
    }

    // 공지글 리스트 조회
    @GetMapping("/list")
    public ResponseEntity<List<ViewNoticeDto>> getNoticeList() {
        List<ViewNoticeDto> noticeList = noticeService.getNoticeList();
        return ResponseEntity.ok(noticeList);
    }

    // 공지글 검색
    @GetMapping("/search")
    public List<ViewNoticeDto> searchNotices(@RequestParam String keyword) {
        return noticeService.searchNotices(keyword);
    }




}