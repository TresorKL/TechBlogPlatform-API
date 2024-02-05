package com.example.techblogplatformapi.collections;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Builder
@Document(collection = "post")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Post {

    @Id
    private String id;
    private String blogId;
    private boolean visibility;
    private PostStatus postStatus;
    private Date creationDate;
    private String title;
    String textContent;
    MetaContent metaContents;
    List<Like>likes;
    List<Comment>comments;
}
