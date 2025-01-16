package com.boot.swlugweb.v1.blog;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.awt.print.Pageable;
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
    public ResponseEntity<List<BlogDto>> getBlogs(@RequestParam(defaultValue = "1", required = false) int page) {
        if (page < 1) {
            page = 1;
        }
        int zeroBasedPage = page - 1;
        List<BlogDto> blogList = blogService.getBlogs(zeroBasedPage);

        if (blogList == null || blogList.isEmpty()) {
            return ResponseEntity.noContent().build(); // 데이터가 없으면 204 No Content
        }

        return ResponseEntity.ok(blogList);
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

        blogService.createBlog(blogCreateDto, userId);
        return ResponseEntity.ok().body("{\"redirect\": \"/api/blog\"}");
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

        blogService.updateBlog(blogUpdateRequestDto, userId);
        return ResponseEntity.ok().body("{\"redirect\": \"/blog\"}");
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

        blogService.deleteBlog(blogDeleteRequestDto, userId);
        return ResponseEntity.ok().body("{\"redirect\": \"/blog\"}");
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
}
