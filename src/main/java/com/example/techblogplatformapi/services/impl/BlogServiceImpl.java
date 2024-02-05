package com.example.techblogplatformapi.services.impl;

import com.example.techblogplatformapi.collections.Blog;
import com.example.techblogplatformapi.repositories.BlogRepository;
import com.example.techblogplatformapi.services.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public void createBlog(Blog blog) {
        blogRepository.save(blog);
    }

    @Override
    public Blog getBlog(String id) {
        return blogRepository.findById(id).get();
    }

    @Override
    public List<Blog> getBlogs() {
        return blogRepository.findAll();
    }
}
