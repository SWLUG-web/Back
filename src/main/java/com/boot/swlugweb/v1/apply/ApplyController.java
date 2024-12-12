package com.boot.swlugweb.v1.apply;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplyController {

    @GetMapping("/apply")
    public ResponseEntity<ApplyResponse> apply() {
        // 예시로 true를 반환하는 객체를 반환
        ApplyResponse response = new ApplyResponse(true);

        // Cache-Control 헤더를 'no-store'로 설정하여 캐시 방지
        return ResponseEntity.ok()
                .header(HttpHeaders.CACHE_CONTROL, "no-store")
                .body(response);
    }

    // 응답 형식 클래스 정의
    public static class ApplyResponse {
        private boolean isApply;

        // 생성자
        public ApplyResponse(boolean isApply) {
            this.isApply = isApply;
        }

        // Getter 및 Setter
        public boolean isApply() {
            return isApply;
        }

        public void setIsApply(boolean isApply) {
            this.isApply = isApply;
        }
    }
}
