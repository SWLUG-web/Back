package com.boot.swlugweb.v1.blog;

import com.boot.swlugweb.v1.notice.NoticeDomain;
import com.boot.swlugweb.v1.notice.NoticeSummaryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BlogService {

    private final BlogRepository blogRepository;

    public BlogService(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    public BlogPageResponse getBlogsWithPaginationg(int page, String searchTerm, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<BlogDto> blogPage;
        long totalBlogs = blogRepository.countByBlogCategoryAndIsDelete(0);

        if (searchTerm != null && !searchTerm.isEmpty()) {
            blogPage = blogRepository.findByBlogIsDeleteOrderByIsPinDescCreateAtDesc(0, pageable);
        } else {
            try {
                String decodedSearchTerm = java.net.URLDecoder.decode(searchTerm, "UTF-8");
                String regexPattern = ".*" + decodedSearchTerm.trim()
                        .replaceAll("[\\s]+", " ")
                        .replaceAll(" ", "(?:[ ]|)") + ".*";

                blogPage = blogRepository.findByBlogTitleContainingAndIsDelete(
                        regexPattern, 0, pageable
                );
            } catch (Exception e) {
                throw new RuntimeException("검색어 처리 중 오류가 발생했습니다.", e);
            }
        }

        // 번호 부여
        List<BlogDto> blogsWithNumbers = blogPage.getContent().stream()
                .map(blog -> {
                    // 현제 게시글보다 최신인 게시글 수를 한 번에 조회
                    long olderCount = blogRepository.countOlderBlogs(0, blog.getCreateAt());
                    blog.setDisplayNumber(totalBlogs - olderCount);
                    return blog;
                })
                .collect(Collectors.toList());

        return new BlogPageResponse(
                blogsWithNumbers,
                blogPage.getTotalElements(),
                blogPage.getTotalPages(),
                page
        );
    }

    // 게시물 상세 조회
    public BlogDomain getBlogDetail(String id) {
        BlogDomain blog = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id+" Blog post not found"));

        // 카테고리가 0이면 예외 발생
        if (blog.getBoardCategory() == 0) {
            throw new IllegalArgumentException("Invalid blog category");
        }

        return blog;
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
        return blogRepository.save(blogDomain);
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
        blogRepository.save(blog);
    }

    // blog 삭제
    public void deleteBlog(BlogDeleteRequestDto blogDeleteRequestDto, String userId) {
        // 1. ID로 해당 블로그 조회
        String id = blogDeleteRequestDto.getId();

        BlogDomain blog = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Blog post not found: " + id));

        // 2. 작성자(userId) 확인
        if (!blog.getUserId().equals(userId)) {
            throw new SecurityException("You are not authorized to delete this post.");
        }

        // 3. 데이터 삭제
        blogRepository.deleteById(id);
    }

    public Map<String, BlogSummaryDto> getAdjacentBlogs(String id) {
        Map<String, BlogSummaryDto> result = new HashMap<>();

        BlogDomain currentBlog = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + " 게시물이 없습니다."));
        LocalDateTime currentCreateAt = currentBlog.getCreateAt();

        List<BlogDomain> prevBlogs = blogRepository.findPrevBlogs(currentCreateAt);
        if (!prevBlogs.isEmpty()) {
            BlogDomain prevBlog = prevBlogs.stream()
                    .min((a, b) -> a.getCreateAt().compareTo(b.getCreateAt()))
                    .get();
            BlogSummaryDto prevDto = new BlogSummaryDto();
            prevDto.setId(prevBlog.getId());
            prevDto.setBlogTitle(prevBlog.getBoardTitle());
            result.put("previous", prevDto);
        }

        List<BlogDomain> nextBlogs = blogRepository.findNextBlogs(currentCreateAt);
        if (!nextBlogs.isEmpty()) {
            BlogDomain nextBlog = nextBlogs.stream()
                    .max((a, b) -> a.getCreateAt().compareTo(b.getCreateAt()))
                    .get();
            BlogSummaryDto nextDto = new BlogSummaryDto();
            nextDto.setId(nextBlog.getId());
            nextDto.setBlogTitle(nextBlog.getBoardTitle());
            result.put("next", nextDto);
        }

        return result;

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
