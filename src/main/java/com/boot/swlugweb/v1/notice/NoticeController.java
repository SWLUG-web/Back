package com.boot.swlugweb.v1.notice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping
    public ResponseEntity<List<NoticeDto>> getNoticeById(@RequestParam(defaultValue = "1", required = false) int page) {
        if (page < 1) {
            page = 1;
        }
        int zeroBasedPage = page - 1;
        List<NoticeDto> noticeList = noticeService.getNotices(zeroBasedPage);

        if (noticeList == null || noticeList.isEmpty()) {
            return ResponseEntity.noContent().build(); // 데이터가 없으면 204 No Content
        }

        return ResponseEntity.ok(noticeList);
    }

}
