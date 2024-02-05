package com.example.techblogplatformapi.collections;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Like {

    private String likedBy;
    private Date likedAt;
}
