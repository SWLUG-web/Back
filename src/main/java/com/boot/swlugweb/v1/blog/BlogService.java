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

    private String getCategoryName(Integer category) {
        switch (category) {
            case 1: return "성과";
            case 2: return "정보";
            case 3: return "후기";
            case 4: return "활동";
            default: return "";
        }
    }

    public BlogPageResponseDto getBlogsWithPaginationg(int page, Integer category, String searchTerm, int size, List<String> tags) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<BlogDto> blogPage;
        List<Integer> categories;

        if (category == null || category < 1 || category > 4) {
            categories = Arrays.asList(1, 2, 3, 4);
        } else {
            categories = List.of(category);
        }

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            if (tags == null || tags.isEmpty()) {
                blogPage = blogRepository.findByBlogIsDeleteOrderByIsPinDescCreateAtDesc(categories, 0, pageable);
            } else {
                blogPage = blogRepository.findByBlogIsDeleteOrderByIsPinDescCreateAtDescAndTag(categories, tags, 0, pageable);
            }
        } else {
            try {
                String decodedSearchTerm = java.net.URLDecoder.decode(searchTerm, "UTF-8");
                String regexPattern = ".*" + decodedSearchTerm.trim()
                        .replaceAll("[\\s]+", " ")
                        .replaceAll(" ", "(?:[ ]|)") + ".*";

                if (tags == null || tags.isEmpty()) {
                    blogPage = blogRepository.findByBlogTitleContainingAndIsDelete(
                            categories, regexPattern, 0, pageable
                    );
                } else {
                    blogPage = blogRepository.findByBlogTitleContainingAndIsDeleteAndTag(categories, regexPattern, tags, 0, pageable);
                }

            } catch (Exception e) {
                throw new RuntimeException("검색어 처리 중 오류가 발생했습니다.", e);
            }
        }

        // 닉네임과 카테고리 이름을 설정
        List<BlogDto> blogsWithInfo = blogPage.getContent().stream()
                .map(blog -> {
                    String nickname = myPageRepository.findNickname(blog.getUserId());
                    blog.setNickname(nickname);
                    blog.setCategoryName(getCategoryName(blog.getBoardCategory()));
                    return blog;
                })
                .collect(Collectors.toList());

        return new BlogPageResponseDto(
                blogsWithInfo,
                blogPage.getTotalElements(),
                blogPage.getTotalPages(),
                page
        );
    }

    public BlogDetailResponseDto getBlogDetail(String id) {
        BlogDomain blog = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id+" Blog post not found"));

        String nickname = myPageRepository.findNickname(blog.getUserId());

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

    public BlogDomain createBlog(BlogCreateDto blogCreateDto, String userId) throws GeneralSecurityException, IOException {
        BlogDomain blogDomain = new BlogDomain();

        blogDomain.setUserId(userId);
        blogDomain.setBoardCategory(blogCreateDto.getBoardCategory());
        blogDomain.setBoardTitle(blogCreateDto.getBoardTitle());
        blogDomain.setBoardContents(blogCreateDto.getBoardContent());
        blogDomain.setCreateAt(LocalDateTime.now());
        blogDomain.setTag(blogCreateDto.getTag());
        blogDomain.setIsPin(false);
        blogDomain.setIsSecure(0);
        blogDomain.setIsDelete(0);

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

        return blogRepository.save(blogDomain);
    }

    public void updateBlog(BlogUpdateRequestDto blogUpdateRequestDto, String userId) throws GeneralSecurityException, IOException {
        String id = blogUpdateRequestDto.getId();

        BlogDomain blog = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + "게시물이 없습니다."));

        if(!blog.getUserId().equals(userId)) {
            throw new SecurityException("작성자가 아닙니다.");
        }

        if (blogUpdateRequestDto.getBoardTitle() != null) {
            blog.setBoardTitle(blogUpdateRequestDto.getBoardTitle());
        }
        if (blogUpdateRequestDto.getBoardContent() != null) {
            blog.setBoardContents(blogUpdateRequestDto.getBoardContent());
        }
        if (blogUpdateRequestDto.getTag() != null) {
            blog.setTag(blogUpdateRequestDto.getTag());
        }

        List<String> currentImageUrls = blog.getImage() != null ? new ArrayList<>(blog.getImage()) : new ArrayList<>();
        List<String> updatedImageUrls = blogUpdateRequestDto.getImageUrls() != null ? blogUpdateRequestDto.getImageUrls() : new ArrayList<>();
        List<String> imagesToDelete = new ArrayList<>(currentImageUrls);
        imagesToDelete.removeAll(updatedImageUrls);

        for (String imageUrl : imagesToDelete) {
            String fileId = extractFileIdFromUrl(imageUrl);
            try {
                googleDriveService.deleteFile(fileId);
            } catch (Exception e) {
                System.err.println("이미지 삭제 실패: " + imageUrl);
                e.printStackTrace();
            }
        }

        if (blogUpdateRequestDto.getImageFiles() != null) {
            for (MultipartFile file : blogUpdateRequestDto.getImageFiles()) {
                String imageUrl = googleDriveService.uploadFile(file);
                updatedImageUrls.add(imageUrl);
            }
        }

        blog.setImage(updatedImageUrls);
        blog.setCreateAt(LocalDateTime.now());

        blogRepository.save(blog);
    }

    private String extractFileIdFromUrl(String imageUrl) {
        String prefix = "https://drive.google.com/uc?id=";
        if (imageUrl != null && imageUrl.startsWith(prefix)) {
            return imageUrl.substring(prefix.length());
        }
        return null;
    }

    public void deleteBlog(BlogDeleteRequestDto blogDeleteRequestDto, String userId) throws GeneralSecurityException, IOException {
        String id = blogDeleteRequestDto.getId();

        BlogDomain blog = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Blog post not found: " + id));

        if (!blog.getUserId().equals(userId)) {
            throw new SecurityException("You are not authorized to delete this post.");
        }

        List<String> imageUrls = blog.getImage();
        if (imageUrls != null && !imageUrls.isEmpty()) {
            for (String imageUrl : imageUrls) {
                String fileId = extractFileIdFromUrl(imageUrl);
                if (fileId != null) {
                    googleDriveService.deleteFile(fileId);
                }
            }
        }

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

    public List<String> getAllTags() {
        return blogRepository.findAllTags();
    }
}