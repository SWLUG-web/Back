package com.boot.swlugweb.v1.blog;

import com.boot.swlugweb.v1.main.MainRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogService {

    private final BlogRepository blogRepository;

    public BlogService(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    public List<BlogDto> getBlogs() {
        return blogRepository.findByBoard();
    }

    public List<BlogDto> getBlogs(int page) {
        Pageable pageable = PageRequest.of(page, 8);
        return blogRepository.findByBoardCategory(pageable);
    }
}
