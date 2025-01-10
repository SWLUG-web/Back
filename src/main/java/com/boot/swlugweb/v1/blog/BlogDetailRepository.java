package com.boot.swlugweb.v1.blog;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface BlogDetailRepository extends MongoRepository<BlogDomain, String> {
}
