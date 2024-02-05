package com.example.techblogplatformapi.services.impl;

import com.example.techblogplatformapi.collections.Blog;
import com.example.techblogplatformapi.collections.Comment;
import com.example.techblogplatformapi.collections.MetaContent;
import com.example.techblogplatformapi.collections.Post;
import com.example.techblogplatformapi.dto.AddCommentRequest;
import com.example.techblogplatformapi.dto.PostObjectSQS;
import com.example.techblogplatformapi.repositories.BlogRepository;
import com.example.techblogplatformapi.repositories.PostRepository;
import com.example.techblogplatformapi.services.PostService;
import com.example.techblogplatformapi.services.StorageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private StorageService storageService;

    @Autowired
    private QueueMessagingTemplate queueMessagingTemplate;


    @Autowired
    private ObjectMapper objectMapper;

    @Value("${cloud.aws.end-point.uri}")
    private String endpoint;

    @Override
    public String addPost(Post post) {

        // Save new Post
        post.setVisibility(false);
        post.setCreationDate(new Date());

        Post newPost = postRepository.save(post);

        // Link Post to its parent Blog
        Blog blog= blogRepository.findById(post.getBlogId()).get();

        List<String >postIds=blog.getPostIds();

        if (postIds==null){
            postIds=new ArrayList<>();
        }


        postIds.add(newPost.getId());
        blog.setPostIds(postIds);


        blogRepository.save(blog);


        return "New Post successfully added";

    }

    public String uploadPostImage( String postId, MultipartFile file, HttpServletRequest request){

        // Store Post Image to S3

        String fileName = storageService.uploadFile(file);
        String download_url= request.getServerName()+"/api/post/image/"+fileName;

        MetaContent metaContent = MetaContent.builder()
                .name(fileName)
                .download_url(download_url)
                .build();

        Optional<Post> post = postRepository.findById(postId);

        if (post.isPresent()) {

            post.get().setMetaContents(metaContent);
            postRepository.save(post.get());

            return "Image successfully uploaded...";
        }else {
            return "Error uploading image";
        }


    }

    @Override
    public Post getPost(String postId) {
        Optional<Post> post=postRepository.findById(postId);
        return post.orElse(null);
    }

    @Override
    public List<Post> getAllPostFromBlog(String blogId) {

        Blog blog = blogRepository.findById(blogId).get();
        List<Post>posts=new ArrayList<>();

        for (int i=0;i<blog.getPostIds().size();i++){

            Post post =postRepository.findById(blog.getPostIds().get(i)).get();
            posts.add(post);
        }

        return posts;
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public String addComment(AddCommentRequest addCommentRequest) {

        Post post=postRepository.findById(addCommentRequest.getPostId()).get();


        Comment comment=Comment.builder()
                .commentBody(addCommentRequest.getCommentBody())
                .commentedBy(addCommentRequest.getCommentedBy())
                .build();

        List<Comment>comments=post.getComments();
        if (comments==null){
            comments=new ArrayList<>();
        }
        comments.add(comment);
        post.setComments(comments);

        postRepository.save(post);

        return "Post successfully added";


    }

    @Override
    public String delete(String id) {

        Optional<Post> post = postRepository.findById(id);

        if (post.isPresent()){
            postRepository.delete(post.get());

            return "Post successfully deleted";
        }else{
          return "Post not found";
        }




    }

    @Override
    public String[] publishPost(String id) throws JsonProcessingException {

        String [] responseArray = new String[2];
        Optional<Post> post =postRepository.findById(id);

        if (post.isPresent()){

            PostObjectSQS postObjectSQS =  PostObjectSQS.builder()
                    .id(post.get().getId())
                    .blogId(post.get().getBlogId())
                    .title(post.get().getTitle())
                    .metaContents(post.get().getMetaContents())
                    .textContent(post.get().getTextContent())
                    .build();


            String postObjectJson = objectMapper.writeValueAsString(postObjectSQS);

            Message<String> message = MessageBuilder.withPayload(postObjectJson).build();

            //queueMessagingTemplate.send(endpoint, MessageBuilder.withPayload(postObjectSQS).build());

            queueMessagingTemplate.send(endpoint, message);

            responseArray[0]="200";
            responseArray[1]="Post successfully sent";

           return responseArray;
        }else {
            responseArray[0]="404";
            responseArray[1]="Post not found";

            return responseArray;
        }

    }
}
