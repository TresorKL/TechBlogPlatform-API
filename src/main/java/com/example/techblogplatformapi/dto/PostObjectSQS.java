package com.example.techblogplatformapi.dto;

import com.example.techblogplatformapi.collections.MetaContent;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostObjectSQS {

    private String id;
    private String blogId;
    private String title;
    private String textContent;
    private MetaContent metaContents;
}
