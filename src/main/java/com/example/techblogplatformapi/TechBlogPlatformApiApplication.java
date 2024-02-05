package com.example.techblogplatformapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class TechBlogPlatformApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TechBlogPlatformApiApplication.class, args);
    }

}
