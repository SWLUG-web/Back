package com.boot.swlugweb.v1.FAQ;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FaqController {

    @GetMapping("/faq")
    public ResponseEntity<Integer> faq() {
        // 예시로 0을 반환하는 응답
        int response = 0;

        // Cache-Control 헤더를 'no-store'로 설정하여 캐시 방지
        return ResponseEntity.ok()
                .header(HttpHeaders.CACHE_CONTROL, "no-store")
                .body(response);
    }
}
