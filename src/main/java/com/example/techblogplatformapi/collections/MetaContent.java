package com.example.techblogplatformapi.collections;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MetaContent {

    private String name;
    private String download_url;

}
