package com.example.techblogplatformapi.repositories;

import com.example.techblogplatformapi.collections.Blog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends MongoRepository<Blog,String> {
}
