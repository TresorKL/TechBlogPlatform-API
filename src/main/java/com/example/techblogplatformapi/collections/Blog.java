package com.example.techblogplatformapi.collections;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document(collection = "blog")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Blog {

    @Id
    private String id;
    private String name;
    private String description;
    private Author author;
    List<String> postIds;
    List<Subscriber>subscribers;

}
