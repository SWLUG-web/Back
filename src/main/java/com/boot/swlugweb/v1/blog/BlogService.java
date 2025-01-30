package com.boot.swlugweb.v1.blog;

import com.boot.swlugweb.v1.mypage.MyPageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BlogService {

    private final BlogRepository blogRepository;
    private final GoogleDriveService googleDriveService;
    private final MyPageRepository myPageRepository;

    public BlogService(BlogRepository blogRepository, GoogleDriveService googleDriveService, MyPageRepository myPageRepository) {
        this.blogRepository = blogRepository;
        this.googleDriveService = googleDriveService;
        this.myPageRepository = myPageRepository;
    }

    public BlogPageResponseDto getBlogsWithPaginationg(int page, Integer category, String searchTerm, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<BlogDto> blogPage;
        List<Integer> categories;
        long totalBlogs = blogRepository.countByBlogCategoryAndIsDelete(0);

        // 카테고리가 1~4인지 확인
        if (category == null || category < 1 || category > 4) {
            // 1~4가 아닌 경우 전체 조회 (0 제외)
            categories = Arrays.asList(1, 2, 3, 4);
        } else {
            // 1~4 중 하나면 해당 카테고리만 조회
            categories = List.of(category);
        }

        if (searchTerm == null || searchTerm.isEmpty()) {
            blogPage = blogRepository.findByBlogIsDeleteOrderByIsPinDescCreateAtDesc(categories, 0, pageable);
        } else {
            try {
                String decodedSearchTerm = java.net.URLDecoder.decode(searchTerm, "UTF-8");
                String regexPattern = ".*" + decodedSearchTerm.trim()
                        .replaceAll("[\\s]+", " ")
                        .replaceAll(" ", "(?:[ ]|)") + ".*";

                blogPage = blogRepository.findByBlogTitleContainingAndIsDelete(
                        categories, regexPattern, 0, pageable
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

        return new BlogPageResponseDto(
                blogsWithNumbers,
                blogPage.getTotalElements(),
                blogPage.getTotalPages(),
                page
        );
    }

    // 게시물 상세 조회
    public BlogDetailResponseDto getBlogDetail(String id) {
        BlogDomain blog = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id+" Blog post not found"));

        String nickname = myPageRepository.findNickname(blog.getUserId());

        // 카테고리가 0이면 예외 발생
        if (blog.getBoardCategory() == 0) {
            throw new IllegalArgumentException("Invalid blog category");
        }

        BlogDetailResponseDto blogDetailResponseDto = new BlogDetailResponseDto();

        blogDetailResponseDto.setId(blog.getId());
        blogDetailResponseDto.setUserId(blog.getUserId());
        blogDetailResponseDto.setBoardTitle(blog.getBoardTitle());
        blogDetailResponseDto.setBoardCategory(blog.getBoardCategory());
        blogDetailResponseDto.setBoardContents(blog.getBoardContents());
        blogDetailResponseDto.setNickname(nickname);
        blogDetailResponseDto.setCreateAt(blog.getCreateAt());
        blogDetailResponseDto.setTag(blog.getTag());
        blogDetailResponseDto.setImage(blog.getImage());

        return blogDetailResponseDto;
    }

//    // 게시물 저장
//    public BlogDomain createBlog(BlogCreateDto blogCreateDto, String userId) {
//        BlogDomain blogDomain = new BlogDomain();
//
//        blogDomain.setUserId(userId);
//        blogDomain.setBoardCategory(blogCreateDto.getBoardCategory());
//        blogDomain.setBoardTitle(blogCreateDto.getBoardTitle());
//        blogDomain.setBoardContents(blogCreateDto.getBoardContent());
//        blogDomain.setCreateAt(LocalDateTime.now());
//        blogDomain.setTag(blogCreateDto.getTag());
//        blogDomain.setImage(blogCreateDto.getImageUrl());
//        blogDomain.setIsPin(false);
//        blogDomain.setIsSecure(0);
//        blogDomain.setIsDelete(0);
//
//        // MongoDB에 저장
//        return blogRepository.save(blogDomain);
//    }

//    블로그 생성  google
    public BlogDomain createBlog(BlogCreateDto blogCreateDto, String userId) throws GeneralSecurityException, IOException {
        BlogDomain blogDomain = new BlogDomain();

        // 기본 필드 설정
        blogDomain.setUserId(userId);
        blogDomain.setBoardCategory(blogCreateDto.getBoardCategory());
        blogDomain.setBoardTitle(blogCreateDto.getBoardTitle());
        blogDomain.setBoardContents(blogCreateDto.getBoardContent());
        blogDomain.setCreateAt(LocalDateTime.now());
        blogDomain.setTag(blogCreateDto.getTag());
        blogDomain.setIsPin(false);
        blogDomain.setIsSecure(0);
        blogDomain.setIsDelete(0);

        // Google Drive 이미지 업로드
        List<String> uploadedImageUrls = new ArrayList<>();
        if (blogCreateDto.getImageFiles() != null && !blogCreateDto.getImageFiles().isEmpty()) {
            for (MultipartFile file : blogCreateDto.getImageFiles()) {
                try {
                    String imageUrl = googleDriveService.uploadFile(file);
                    uploadedImageUrls.add(imageUrl);
                } catch (Exception e) {
                    System.err.println("이미지 업로드 실패: " + file.getOriginalFilename());
                    e.printStackTrace();
                }
            }
        }

        blogDomain.setImage(uploadedImageUrls);

        // MongoDB 저장
        return blogRepository.save(blogDomain);
    }



////     게시물 수정
//    public void updateBlog(BlogUpdateRequestDto blogUpdateRequestDto, String userId) {
//        // 기존 데이터 조회
//        String id = blogUpdateRequestDto.getId();
//
//        BlogDomain blog = blogRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException(id + "게시물이 없습니다."));
//
//        // 작성자(userId) 확인
//        if(!blog.getUserId().equals(userId)) {
//            throw new SecurityException("작성자가 아닙니다.");
//        }
//
//        // 필드 업데이트
//        if (blogUpdateRequestDto.getBoardTitle() != null) {
//            blog.setBoardTitle(blogUpdateRequestDto.getBoardTitle());
//        }
//        if (blogUpdateRequestDto.getBoardContent() != null) {
//            blog.setBoardCategory(blogUpdateRequestDto.getBoardCategory());
//        }
//        if (blogUpdateRequestDto.getBoardContent() != null) {
//            blog.setBoardContents(blogUpdateRequestDto.getBoardContent());
//        }
//        if (blogUpdateRequestDto.getTag() != null) {
//            blog.setTag(blogUpdateRequestDto.getTag());
//        }
//        if (blogUpdateRequestDto.getImageUrl() != null) {
//            blog.setImage(blogUpdateRequestDto.getImageUrl());
//        }
//        blog.setCreateAt(LocalDateTime.now());
//
//        // 저장
//        blogRepository.save(blog);
//    }

    // 게시물 수정 google
    public void updateBlog(BlogUpdateRequestDto blogUpdateRequestDto, String userId) throws GeneralSecurityException, IOException {
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
        // 기존 이미지 URL 목록
        List<String> currentImageUrls = blog.getImage() != null ? new ArrayList<>(blog.getImage()) : new ArrayList<>();

        // 유지하려는 이미지 URL 목록
        List<String> updatedImageUrls = blogUpdateRequestDto.getImageUrls() != null ? blogUpdateRequestDto.getImageUrls() : new ArrayList<>();

        // 삭제할 이미지 URL 계산
        List<String> imagesToDelete = new ArrayList<>(currentImageUrls);
        imagesToDelete.removeAll(updatedImageUrls);

        // Google Drive에서 삭제
        for (String imageUrl : imagesToDelete) {
            String fileId = extractFileIdFromUrl(imageUrl);
            try {
                googleDriveService.deleteFile(fileId); // 삭제
            } catch (Exception e) {
                System.err.println("이미지 삭제 실패: " + imageUrl);
                e.printStackTrace();
            }
        }
        // 새로 업로드된 이미지 추가
        if (blogUpdateRequestDto.getImageFiles() != null) {
            for (MultipartFile file : blogUpdateRequestDto.getImageFiles()) {
                String imageUrl = googleDriveService.uploadFile(file);
                updatedImageUrls.add(imageUrl);
            }
        }

        // 최종 이미지 목록 설정
        blog.setImage(updatedImageUrls);
        blog.setCreateAt(LocalDateTime.now());

        // 저장
        blogRepository.save(blog);
    }

//    private String extractFileIdFromUrl(String url) {
//        // Google Drive URL에서 파일 ID 추출
//        String[] parts = url.split("/");
//        return parts[parts.length - 2];
//    }

    private String extractFileIdFromUrl(String imageUrl) {
        String prefix = "https://drive.google.com/uc?id=";
        if (imageUrl != null && imageUrl.startsWith(prefix)) {
            return imageUrl.substring(prefix.length());
        }
        return null;
    }



//    // blog 삭제
//    public void deleteBlog(BlogDeleteRequestDto blogDeleteRequestDto, String userId) {
//        // 1. ID로 해당 블로그 조회
//        String id = blogDeleteRequestDto.getId();
//
//        BlogDomain blog = blogRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Blog post not found: " + id));
//
//        // 2. 작성자(userId) 확인
//        if (!blog.getUserId().equals(userId)) {
//            throw new SecurityException("You are not authorized to delete this post.");
//        }
//
//        // 3. 데이터 삭제
//        blogRepository.deleteById(id);
//    }

    //블로그 삭제 google
    public void deleteBlog(BlogDeleteRequestDto blogDeleteRequestDto, String userId) throws GeneralSecurityException, IOException {
        // 1. ID로 해당 블로그 조회
        String id = blogDeleteRequestDto.getId();

        BlogDomain blog = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Blog post not found: " + id));

        // 2. 작성자(userId) 확인
        if (!blog.getUserId().equals(userId)) {
            throw new SecurityException("You are not authorized to delete this post.");
        }

        // 3. Google Drive에서 이미지 삭제
        List<String> imageUrls = blog.getImage();  // 이미지 URL 목록
        if (imageUrls != null && !imageUrls.isEmpty()) {
            for (String imageUrl : imageUrls) {
                // URL에서 fileId 추출
                String fileId = extractFileIdFromUrl(imageUrl);
                if (fileId != null) {
                    // Google Drive에서 파일 삭제
                    googleDriveService.deleteFile(fileId);
                }
            }
        }

        // 4. 데이터 삭제
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

//    // 카테고리 검색
//    public List<BlogDomain> searchBlogsByCategory(int category, int page) {
//        if (category < 0) {
//            throw new IllegalArgumentException("Category must be non-negative");
//        }
//        if (page < 0) {
//            throw new IllegalArgumentException("Page number cannot be negative");
//        }
//        Pageable pageable = PageRequest.of(page, 5);
//        List<BlogDomain> results = blogRepository.findByCategory(category, pageable);
//        return results.isEmpty() ? Collections.emptyList() : results;
//    }

}
