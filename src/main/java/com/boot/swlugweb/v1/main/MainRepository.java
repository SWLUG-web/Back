package com.boot.swlugweb.v1.main;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MainRepository extends MongoRepository<MainDomain, String> {
    @Query(value = " {'board_category': 0}", sort = "{ 'created_at': -1 } ")
    List<MainDomain> findTop3ByCategoryOrderByCreatedAtDesc();
}
