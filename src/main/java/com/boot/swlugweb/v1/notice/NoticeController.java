package com.boot.swlugweb.v1.notice;

import com.boot.swlugweb.v1.blog.BlogDomain;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notice")
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

    @PostMapping("/detail")
    public ResponseEntity<NoticeDomain> getNoticeDetail(@RequestBody Map<String, String> request) {
        String id = request.get("id");

        NoticeDomain notice = noticeService.getNoticeDetail(id);
        return ResponseEntity.ok(notice);
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveNotice(@RequestBody NoticeCreateDto noticeCreateDto,
                                        HttpSession session) {
        String userId = (String) session.getAttribute("USER");
//        String roleType = (String) session.getAttribute("ROLE");
//        System.out.println(roleType);
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }
//        // roleType이 null이거나 "admin"이 아닌 경우 401 Unauthorized 반환
//        if (roleType == null || !roleType.equalsIgnoreCase("admin")) {
//            return ResponseEntity.status(401).body("Unauthorized: Admin access required.");
//        }

        noticeService.createNotice(noticeCreateDto, userId);
        return ResponseEntity.ok().body("{\"redirect\": \"/api/notice\"}");

    }

}
