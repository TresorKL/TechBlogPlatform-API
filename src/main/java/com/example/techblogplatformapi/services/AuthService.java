package com.example.techblogplatformapi.services;


import com.example.techblogplatformapi.dto.JwtAuthResponse;
import com.example.techblogplatformapi.dto.RefreshTokenRequest;
import com.example.techblogplatformapi.dto.SignInRequest;
import com.example.techblogplatformapi.dto.SignUpRequest;
import com.example.techblogplatformapi.entity.User;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessagingException;
import org.springframework.web.bind.annotation.RequestBody;


public interface AuthService {

    User signUp(SignUpRequest signUpRequest) throws MessagingException;
    JwtAuthResponse signIn(SignInRequest signInRequest) throws MessagingException;

    JwtAuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
