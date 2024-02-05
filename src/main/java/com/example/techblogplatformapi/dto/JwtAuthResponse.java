package com.example.techblogplatformapi.dto;

import lombok.Data;

@Data
public class JwtAuthResponse {

    private String token;
    private String refreshToken;
    private String message;
}
