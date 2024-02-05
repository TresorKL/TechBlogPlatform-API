package com.example.techblogplatformapi.collections;

import lombok.*;

@Data
@Getter
@Setter
@RequiredArgsConstructor
public class PostStatus {

    private boolean isPostValid;
    private String reason;
}
