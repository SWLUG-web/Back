
package com.boot.swlugweb.v1.notice.domain.repository;

import com.boot.swlugweb.v1.notice.domain.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}

