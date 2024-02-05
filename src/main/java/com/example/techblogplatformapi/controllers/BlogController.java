package com.example.techblogplatformapi.controllers;

import com.example.techblogplatformapi.collections.Blog;
import com.example.techblogplatformapi.services.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/blog")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @PostMapping
    public void createBlog(@RequestBody Blog blog){
        blogService.createBlog(blog);
    }

    @GetMapping("/{id}")
    public Blog getBlog(@PathVariable("id") String id){

        return blogService.getBlog(id);
    }

    @GetMapping("/all")
    public List<Blog> getBlogs(){
        return blogService.getBlogs();
    }
}
