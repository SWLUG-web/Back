package com.boot.swlugweb.v1.blog;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/blog")
public class BlogController {

    @Autowired
    private final BlogService blogService;

    private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private final GoogleDriveService googleDriveService;


    public BlogController(BlogService blogService, GoogleDriveService googleDriveService) {
        this.blogService = blogService;
        this.googleDriveService = googleDriveService;
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

//    @PostMapping("/detail")
//    public ResponseEntity<BlogDomain> getBlogDetail(@RequestBody Map<String, String> request) {
//        String id = request.get("id");
//
//        BlogDomain blog = blogService.getBlogDetail(id);
//        return ResponseEntity.ok(blog);
//    }

//    @PostMapping("/detail")
//    public ResponseEntity<BlogDto> getBlogDetail(@RequestBody Map<String, String> request) {
//        String id = request.get("id");
//
//        BlogDto blog = blogService.getBlogDetail(id);
//        return ResponseEntity.ok(blog);
//    }


    @PostMapping("/save")
    public ResponseEntity<?> saveBlog(@RequestBody BlogCreateDto blogCreateDto,
                                 HttpSession session) throws GeneralSecurityException , IOException {
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
    ) throws GeneralSecurityException, IOException {
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

    @PostMapping("/adjacent")
    public ResponseEntity<Map<String, BlogSummaryDto>> searchBlogsByAdjacent(@RequestBody Map<String, String> request) {
        String id = request.get("id");
        Map<String, BlogSummaryDto> adjacentBlogs = blogService.getAdjacentBlogs(id);
        return ResponseEntity.ok(adjacentBlogs);
    }

//    @PostMapping("/upload-image")
//    public ResponseEntity<?> uploadImage(@RequestParam("upload") MultipartFile imageFile) {
//        try {
//            String fileId = googleDriveService.uploadFile(imageFile);
//            String fileUrl = "https://drive.google.com/uc?id=" + fileId;
//            return ResponseEntity.ok(Map.of("uploaded", true, "url", fileUrl));
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body(Map.of("uploaded", false, "error", Map.of("message", e.getMessage())));
//        }
//    }
}
