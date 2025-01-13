package com.boot.swlugweb.v1.notice;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends MongoRepository<NoticeDomain, String> {
    @Query(value = "{ 'board_category' : 0 }")
    List<NoticeDomain> findByNotice();

    @Query(value = "{ 'board_category' : 0 }")
    List<NoticeDto> findByNoticeDto(Pageable pageable);
}
