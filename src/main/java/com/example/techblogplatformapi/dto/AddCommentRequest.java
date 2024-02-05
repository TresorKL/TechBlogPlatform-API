package com.example.techblogplatformapi.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddCommentRequest {

    String commentedBy;
    String commentBody;
    String postId;
}
