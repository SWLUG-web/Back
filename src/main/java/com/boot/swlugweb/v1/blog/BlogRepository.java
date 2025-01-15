package com.boot.swlugweb.v1.blog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends MongoRepository<BlogDomain, String> {
    @Query(value = "{ 'boardCategory' : { $ne : 0 } }" )
    List<BlogDto> findByBoardCategory(Pageable pageable);

    @Query(value = "{ 'boardCategory' : { $ne : 0 } }" )
    List<BlogDto> findByBoard();

    @Query("{ 'tag': { $regex: ?0, $options: 'i' } }")
    List<BlogDomain> findByTag(String tag, Pageable pageable);

    @Query("{ 'boardCategory': ?0 }")
    List<BlogDomain> findByCategory(int category, Pageable pageable);
}
