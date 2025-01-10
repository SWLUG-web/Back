package com.boot.swlugweb.v1.blog;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public List<BlogDto> getBlogs(int page) {
        Pageable pageable = PageRequest.of(page, 8);
        return blogRepository.findByBoardCategory(pageable);
    }

    public BlogDomain createBlog(BlogCreateDto blogCreateDto, String userId) {
        BlogDomain blogDomain = new BlogDomain();

        blogDomain.setUserId(userId);
        blogDomain.setBoardCategory(blogCreateDto.getBoardCategory());
        blogDomain.setBoardTitle(blogCreateDto.getBoardTitle());
        blogDomain.setBoardContents(blogCreateDto.getBoardContent());
        blogDomain.setCreateAt(blogCreateDto.getCreateAt() != null ? blogCreateDto.getCreateAt() : LocalDateTime.now());
        blogDomain.setIsPin(false);
        blogDomain.setIsSecure(false);
        blogDomain.setIsDelete(false);

        // MongoDB에 저장
        return blogDetailRepository.save(blogDomain);
    }
}
