package com.boot.swlugweb.v1.main;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MainService {

    private final MainRepository mainRepository;

    public MainService(MainRepository mainRepository) {
        this.mainRepository = mainRepository;
    }

    public List<MainDomain> getLatestPosts() {
        // Pageable로 최신순 정렬 후 3개 가져오기
        Pageable pageable = PageRequest.of(0, 3, Sort.by("created_at").descending());
        return mainRepository.findByBoardCategoryOrderByCreatedAtDesc(pageable);
    }
}
