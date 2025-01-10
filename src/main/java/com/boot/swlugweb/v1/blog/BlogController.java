package com.boot.swlugweb.v1.blog;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blog")
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
        page = page -1;
        List<BlogDto> blogList = blogService.getBlogs(page);

        return ResponseEntity.ok(blogList).getBody();
    }
}
