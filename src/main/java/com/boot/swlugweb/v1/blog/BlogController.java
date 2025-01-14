package com.boot.swlugweb.v1.blog;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/blog")
public class BlogController {

    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping
    public List<BlogDto> getBlogs() {

        List<BlogDto> blogList = blogService.getBlogs();

        return ResponseEntity.ok(blogList).getBody();
    }

    @GetMapping("/{page}")
    public List<BlogDto> getBlogsByPage(@PathVariable int page) {
        // page가 0보다 작으면 0으로 설정
        if (page < 0) {
            page = 0;
        } else {
            page = page - 1;
        }
        List<BlogDto> blogList = blogService.getBlogs(page);

        return ResponseEntity.ok(blogList).getBody();
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
}
