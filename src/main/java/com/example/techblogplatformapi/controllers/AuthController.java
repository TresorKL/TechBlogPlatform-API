package com.example.techblogplatformapi.controllers;


import com.example.techblogplatformapi.dto.JwtAuthResponse;
import com.example.techblogplatformapi.dto.RefreshTokenRequest;
import com.example.techblogplatformapi.dto.SignInRequest;
import com.example.techblogplatformapi.dto.SignUpRequest;
import com.example.techblogplatformapi.entity.User;
import com.example.techblogplatformapi.repositories.UserRepo;
import com.example.techblogplatformapi.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessagingException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/auth",consumes = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Autowired
    private UserRepo userRepo;

//    @Autowired
//    private EmailVerificationService emailVerificationService;

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody SignUpRequest signUpRequest) throws MessagingException {
        return ResponseEntity.ok(authService.signUp(signUpRequest));
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthResponse> signIn(@RequestBody SignInRequest signInRequest)throws MessagingException {
        return ResponseEntity.ok(authService.signIn(signInRequest));
    }



    @PostMapping(value="/refresh",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtAuthResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest){
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest));
    }









}
