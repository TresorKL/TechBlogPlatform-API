package com.example.techblogplatformapi.collections;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Subscriber {

    private int id;
    private String name;
    private String email;

}
