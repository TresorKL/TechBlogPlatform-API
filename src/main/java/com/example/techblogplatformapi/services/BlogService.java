package com.example.techblogplatformapi.services;


import com.example.techblogplatformapi.collections.Blog;

import java.util.List;

public interface BlogService {


    void createBlog(Blog blog);

    Blog getBlog(String id);

    List<Blog> getBlogs();

}
