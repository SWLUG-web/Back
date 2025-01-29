package com.boot.swlugweb.v1.blog;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
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
    public ResponseEntity<BlogPageResponseDto> getBlogs(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String searchTerm,
            @RequestParam(defaultValue = "10") int size
    ){
        BlogPageResponseDto response = blogService.getBlogsWithPaginationg(page, searchTerm, size);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/detail")
    public ResponseEntity<BlogDetailResponseDto> getBlogDetail(@RequestBody Map<String, String> request) {
        String id = request.get("id");

        BlogDetailResponseDto blog = blogService.getBlogDetail(id);
        return ResponseEntity.ok(blog);
    }


// google 블로그 저장
    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> saveBlog(
            @RequestPart BlogCreateDto blogCreateDto,
            @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles,
            HttpSession session) throws GeneralSecurityException, IOException {

        String userId = (String) session.getAttribute("USER");
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            if (imageFiles != null && !imageFiles.isEmpty()) {
                blogCreateDto.setImageFiles(imageFiles);
            }
            blogService.createBlog(blogCreateDto, userId);
            return ResponseEntity.status(302)
                    .header(HttpHeaders.LOCATION,"/api/blog")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }




//    기본 블로그 저장
//    @PostMapping("/save")
//    public ResponseEntity<?> saveBlog(@RequestBody BlogCreateDto blogCreateDto,
//                                 HttpSession session) throws GeneralSecurityException , IOException {
//        String userId = (String) session.getAttribute("USER");
//        if (userId == null) {
//            return ResponseEntity.status(401).build();
//        }
//
//        try {
//            blogService.createBlog(blogCreateDto, userId);
//
//            return ResponseEntity.status(302)
//                    .header(HttpHeaders.LOCATION,"/api/blog")
//                    .build();
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//
//    }



    // google 블로그 수정
    @PostMapping(value="/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateBlogPost(
            @RequestPart BlogUpdateRequestDto blogUpdateRequestDto,
            @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles,
            HttpSession session
    ) throws GeneralSecurityException, IOException {
        String userId = (String) session.getAttribute("USER");
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            if(imageFiles != null && !imageFiles.isEmpty()) {
                blogUpdateRequestDto.setImageFiles(imageFiles);
            }
            blogService.updateBlog(blogUpdateRequestDto, userId);

            return ResponseEntity.status(302)
                    .header(HttpHeaders.LOCATION,"/api/blog")
                    .build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //기본 블로그 수정
//    @PostMapping("/update")
//    public ResponseEntity<String> updateBlogPost(
//            @RequestBody BlogUpdateRequestDto blogUpdateRequestDto,
//            HttpSession session
//    ) throws GeneralSecurityException, IOException {
//        String userId = (String) session.getAttribute("USER");
//        if (userId == null) {
//            return ResponseEntity.status(401).build();
//        }
//
//        try {
//            blogService.updateBlog(blogUpdateRequestDto, userId);
//
//            return ResponseEntity.status(302)
//                    .header(HttpHeaders.LOCATION,"/api/blog")
//                    .build();
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }

//    기본 블로그 삭제(google도 적용)
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

    //사진 업로드
    @PostMapping("/image/upload")
    @ResponseBody
    public String uploadImageToDrive(MultipartHttpServletRequest request, HttpServletRequest req) throws Exception {
        Map<String, Object> map = new HashMap<>();

        // 이미지 파일 받아오기
        MultipartFile uploadFile = request.getFile("upload");

        // GoogleDriveService를 통해 파일을 구글 드라이브에 업로드
        String fileUrl = googleDriveService.uploadFileToDrive(uploadFile);

        // 반환할 URL을 map에 넣어줌
        map.put("url", fileUrl);

        return new Gson().toJson(map);
    }

}
