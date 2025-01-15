package com.boot.swlugweb.v1.blog;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class BlogService {

    private final BlogRepository blogRepository;
    private final BlogDetailRepository blogDetailRepository;

    public BlogService(BlogRepository blogRepository, BlogDetailRepository blogDetailRepository) {
        this.blogRepository = blogRepository;
        this.blogDetailRepository = blogDetailRepository;
    }

    public List<BlogDto> getBlogs() {
        return blogRepository.findByBoard();
    }

    // 게시물 간단 조회
    public List<BlogDto> getBlogs(int page) {
        Pageable pageable = PageRequest.of(page, 5);
        return blogRepository.findByBoardCategory(pageable);
    }

    // 게시물 상세 조회
    public BlogDomain getBlogDetail(String id) {
        return blogDetailRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id+" Blog post not found"));
    }

    // 게시물 저장
    public BlogDomain createBlog(BlogCreateDto blogCreateDto, String userId) {
        BlogDomain blogDomain = new BlogDomain();

        blogDomain.setUserId(userId);
        blogDomain.setBoardCategory(blogCreateDto.getBoardCategory());
        blogDomain.setBoardTitle(blogCreateDto.getBoardTitle());
        blogDomain.setBoardContents(blogCreateDto.getBoardContent());
        blogDomain.setCreateAt(LocalDateTime.now());
        blogDomain.setTag(blogCreateDto.getTag());
        blogDomain.setImage(blogCreateDto.getImageUrl());
        blogDomain.setIsPin(false);
        blogDomain.setIsSecure(0);
        blogDomain.setIsDelete(0);

        // MongoDB에 저장
        return blogDetailRepository.save(blogDomain);
    }

    // 게시물 수정
    public void updateBlog(BlogUpdateRequestDto blogUpdateRequestDto, String userId) {
        // 기존 데이터 조회
        String id = blogUpdateRequestDto.getId();

        BlogDomain blog = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + "게시물이 없습니다."));

        // 작성자(userId) 확인
        if(!blog.getUserId().equals(userId)) {
            throw new SecurityException("작성자가 아닙니다.");
        }

        // 필드 업데이트
        if (blogUpdateRequestDto.getBoardTitle() != null) {
            blog.setBoardTitle(blogUpdateRequestDto.getBoardTitle());
        }
        if (blogUpdateRequestDto.getBoardContent() != null) {
            blog.setBoardContents(blogUpdateRequestDto.getBoardContent());
        }
        if (blogUpdateRequestDto.getTag() != null) {
            blog.setTag(blogUpdateRequestDto.getTag());
        }
        if (blogUpdateRequestDto.getImageUrl() != null) {
            blog.setImage(blogUpdateRequestDto.getImageUrl());
        }
        blog.setCreateAt(LocalDateTime.now());

        // 저장
        blogDetailRepository.save(blog);
    }

    // blog 삭제
    public void deleteBlog(BlogDeleteRequestDto blogDeleteRequestDto, String userId) {
        // 1. ID로 해당 블로그 조회
        String id = blogDeleteRequestDto.getId();

        BlogDomain blog = blogDetailRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Blog post not found: " + id));

        // 2. 작성자(userId) 확인
        if (!blog.getUserId().equals(userId)) {
            throw new SecurityException("You are not authorized to delete this post.");
        }

        // 3. 데이터 삭제
        blogDetailRepository.deleteById(id);
    }

    //태그 검색
    public List<BlogDomain> searchBlogsByTag(String tag, int page) {
        // 태그 값이 없으면 예외 발생
        if (tag == null || tag.trim().isEmpty()) {
            throw new IllegalArgumentException("Tag cannot be null or empty");
        }

        // 페이지 번호 검증
        if (page < 0) {
            throw new IllegalArgumentException("Page number cannot be negative");
        }

        // 페이지네이션 적용
        Pageable pageable = PageRequest.of(page, 5);

        // 태그 검색
        List<BlogDomain> results = blogRepository.findByTag(tag, pageable);

        // 결과가 없을 경우 빈 리스트 반환
        return results.isEmpty() ? Collections.emptyList() : results;
    }

    // 카테고리 검색
    public List<BlogDomain> searchBlogsByCategory(int category, int page) {
        if (category < 0) {
            throw new IllegalArgumentException("Category must be non-negative");
        }
        if (page < 0) {
            throw new IllegalArgumentException("Page number cannot be negative");
        }
        Pageable pageable = PageRequest.of(page, 5);
        List<BlogDomain> results = blogRepository.findByCategory(category, pageable);
        return results.isEmpty() ? Collections.emptyList() : results;
    }

}
