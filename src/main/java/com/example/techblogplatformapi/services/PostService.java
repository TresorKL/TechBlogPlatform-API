package com.example.techblogplatformapi.services;

import com.example.techblogplatformapi.collections.Comment;
import com.example.techblogplatformapi.collections.Post;
import com.example.techblogplatformapi.dto.AddCommentRequest;
import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

public interface PostService {

      String addPost(Post post);

      Post getPost(String postId);
      List<Post>getAllPostFromBlog(String blogId);
      List<Post> getAllPosts();
      String addComment(AddCommentRequest addCommentRequest);

      String uploadPostImage( String postId, MultipartFile file, HttpServletRequest request);

      String delete(String id);

      String [] publishPost(String id) throws JsonProcessingException;
}
