package com.boot.swlugweb.v1.blog;

import com.boot.swlugweb.v1.notice.NoticeDomain;
import com.boot.swlugweb.v1.notice.NoticeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BlogRepository extends MongoRepository<BlogDomain, String> {
    @Query(value = "{ " +
            "'board_category': { $ne : 0 }, " +
            "'board_title': { $regex: ?0, $options: 'i' }, " +
            "'is_delete': ?1 }",
            sort = "{ 'is_pin': -1, 'created_at': -1, '_id': -1 }")
    Page<BlogDto> findByBlogTitleContainingAndIsDelete(
            String searchTerm,
            Integer isDelete,
            Pageable pageable
    );

    @Query(value = "{ 'board_category': { $ne : 0 }, 'is_delete': ?0 }",
            sort = "{ 'is_pin': -1, 'created_at': -1, '_id': -1 }")
    Page<BlogDto> findByBlogIsDeleteOrderByIsPinDescCreateAtDesc(
            Integer isDelete,
            Pageable pageable
    );

    @Query(count = true, value = "{ 'board_category': { $ne : 0 }, 'is_delete': ?0 }")
    long countByBlogCategoryAndIsDelete(Integer isDelete);

    @Query(value = "{ " +
            "'board_category': { $ne : 0 }, " +
            "'is_delete': ?0, " +
            "'created_at': { $gt: ?1 }" +
            "}",
            count = true)
    long countOlderBlogs(int isDelete, LocalDateTime createAt);

    // 이전 게시물 조회 (더 최신 글 중 가장 가까운 1개)
    @Query(value = "{ 'board_category': { $ne : 0 }, 'is_delete': 0, 'created_at': { $gt: ?0 }}")
    List<BlogDomain> findPrevBlogs(LocalDateTime createAt);

    // 다음 게시물 조회 (더 오래된 글 중 가장 가까운 1개)
    @Query(value = "{ 'board_category': { $ne : 0 }, 'is_delete': 0, 'created_at': { $lt: ?0 }}")
    List<BlogDomain> findNextBlogs(LocalDateTime createAt);

    @Query(value = "{ 'tag': { $regex: ?0, $options: 'i' } }", sort = "{ 'created_at' : -1 }")
    List<BlogDomain> findByTag(String tag, Pageable pageable);

    @Query(value = "{ 'boardCategory': ?0 }", sort = "{ 'created_at' : -1 }")
    List<BlogDomain> findByCategory(int category, Pageable pageable);
}
