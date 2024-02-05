package com.example.techblogplatformapi.controllers;


import com.example.techblogplatformapi.collections.Post;
import com.example.techblogplatformapi.dto.AddCommentRequest;
import com.example.techblogplatformapi.services.PostService;
import com.example.techblogplatformapi.services.StorageService;
import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/post")

public class PostController {


    @Autowired
    private PostService postService;
    @Autowired
    StorageService storageService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addPost(@RequestBody Post post ){
        String responseStr = postService.addPost(post);
        return getObjectResponseEntity(null, responseStr,200);
    }

    @PostMapping(value = "/{id}/upload/image",consumes =  MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> uploadPostImage(@PathVariable("id") String id, @RequestPart("file")MultipartFile file, HttpServletRequest request){

        String responseStr = postService.uploadPostImage(id,file,request);
        return  getObjectResponseEntity(null, responseStr,200);
    }


    @GetMapping("/image/{fileName}")
    public ResponseEntity<ByteArrayResource>downloadPostImage(@PathVariable("fileName")String fileName){

        byte [] data= storageService.downloadFile(fileName);

        ByteArrayResource resource= new ByteArrayResource(data);

        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type","application/octet-stream")
                .header("content-disposition","attachment; filename=\""+fileName+"\"")
                .body(resource);
    }


    @PostMapping("/{id}/publish")
    public ResponseEntity<Object> publishPost(@PathVariable("id") String id) throws JsonProcessingException {

        String [] responseArray = postService.publishPost(id);

        return getObjectResponseEntity(null,responseArray[1],Integer.parseInt(responseArray[0]));
    }




    @GetMapping("/{id}")
    public ResponseEntity<Object> getPost(@PathVariable("id")String id){

        Post post=postService.getPost(id);

        if (post!=null){
            return getObjectResponseEntity(post,"post found",200);
        }else {
            return getObjectResponseEntity(null,"post not found",404);
        }

    }

    @PostMapping("/comment")
    public ResponseEntity<Object> addComment(@RequestBody AddCommentRequest addCommentRequest){

        String responseStr = postService.addComment(addCommentRequest);
        return getObjectResponseEntity(null,responseStr,200);
    }




    @GetMapping("all/blog/{id}")
    public ResponseEntity<Object>getAllPostFromBlog(@PathVariable("id")String id){

        List<Post> postList=postService.getAllPostFromBlog(id);

        if (!postList.isEmpty()){
            return getObjectResponseEntity(postList, "Found", 200);
        }else {
            return getObjectResponseEntity(postList, "Empty", 200);
        }
    }

    @GetMapping("/posts")
    public ResponseEntity<Object>getAllPosts(){
        List<Post>allPosts=postService.getAllPosts();
        return getObjectResponseEntity(allPosts,"Found",200);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object>deletePost(@PathVariable("id") String id){

        String responseStr= postService.delete(id);
        int statusCode=200;
        if (responseStr.contains("not")) {
            statusCode=401;
        }
        return getObjectResponseEntity(null,responseStr,statusCode);
    }


    private ResponseEntity<Object> getObjectResponseEntity(Object data,Object message, int statusCode) {

        Object status= HttpStatusCode.valueOf(statusCode).value();

        Map<String, Object> map=buildResponse( message, status,data);

        return new ResponseEntity<Object>(map, HttpStatusCode.valueOf(statusCode));
    }

    protected Map<String, Object> buildResponse(Object message, Object status, Object data){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("message",message);
        map.put("status", status);
        map.put("data", data);

        return map;
    }
}
