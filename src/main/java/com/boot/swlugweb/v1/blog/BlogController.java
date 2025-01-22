package com.boot.swlugweb.v1.blog;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/blog")
public class BlogController {

    @Autowired
    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping
    public ResponseEntity<BlogPageResponse> getBlogs(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String searchTerm,
            @RequestParam(defaultValue = "10") int size
    ){
        BlogPageResponse response = blogService.getBlogsWithPaginationg(page, searchTerm, size);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/detail")
    public ResponseEntity<BlogDomain> getBlogDetail(@RequestBody Map<String, String> request) {
        String id = request.get("id");

        BlogDomain blog = blogService.getBlogDetail(id);
        return ResponseEntity.ok(blog);
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveBlog(@RequestBody BlogCreateDto blogCreateDto,
                                 HttpSession session) {
        String userId = (String) session.getAttribute("USER");
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            blogService.createBlog(blogCreateDto, userId);

            return ResponseEntity.status(302)
                    .header(HttpHeaders.LOCATION,"/api/blog")
                    .build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PostMapping("/update")
    public ResponseEntity<String> updateBlogPost(
            @RequestBody BlogUpdateRequestDto blogUpdateRequestDto,
            HttpSession session
    ) {
        String userId = (String) session.getAttribute("USER");
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            blogService.updateBlog(blogUpdateRequestDto, userId);

            return ResponseEntity.status(302)
                    .header(HttpHeaders.LOCATION,"/api/blog")
                    .build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteBlog(
            @RequestBody BlogDeleteRequestDto blogDeleteRequestDto,
            HttpSession session
    ) {
        String userId = (String) session.getAttribute("USER");
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            blogService.deleteBlog(blogDeleteRequestDto, userId);
//            return ResponseEntity.ok().body("{\"redirect\": \"/blog\"}");
            return ResponseEntity.status(302)
                    .header(HttpHeaders.LOCATION,"/api/blog")
                    .build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 태그 검색
    @GetMapping("/tagSearch")
    public ResponseEntity<List<BlogDomain>> searchBlogsByTag(
            @RequestParam String tag,
            @RequestParam(defaultValue = "0") int page
    ) {
        try {
            List<BlogDomain> blogs = blogService.searchBlogsByTag(tag, page);
            return ResponseEntity.ok(blogs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // 잘못된 입력 처리
        }
    }

    // 카테고리 검색
    @GetMapping("/category")
    public ResponseEntity<List<BlogDomain>> searchBlogsByCategory(
            @RequestParam int category,
            @RequestParam(defaultValue = "0") int page
    ) {
        try {
            List<BlogDomain> blogs = blogService.searchBlogsByCategory(category, page);
            return ResponseEntity.ok(blogs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/adjacent")
    public ResponseEntity<Map<String, BlogSummaryDto>> searchBlogsByAdjacent(@RequestBody Map<String, String> request) {
        String id = request.get("id");
        Map<String, BlogSummaryDto> adjacentBlogs = blogService.getAdjacentBlogs(id);
        return ResponseEntity.ok(adjacentBlogs);
    }
}
