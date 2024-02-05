package com.example.techblogplatformapi.collections;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class Comment {

    private String commentedBy;
    private String commentBody;

}
