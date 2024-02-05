package com.example.techblogplatformapi.services.impl;

import com.example.techblogplatformapi.dto.JwtAuthResponse;
import com.example.techblogplatformapi.dto.RefreshTokenRequest;
import com.example.techblogplatformapi.dto.SignInRequest;
import com.example.techblogplatformapi.dto.SignUpRequest;
import com.example.techblogplatformapi.entity.Role;
import com.example.techblogplatformapi.entity.User;
import com.example.techblogplatformapi.repositories.UserRepo;
import com.example.techblogplatformapi.services.AuthService;
import com.example.techblogplatformapi.services.JwtService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessagingException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;



    //------------------------------------------------------------------------------------------------------------------------------
    // SIGN UP
    //------------------------------------------------------------------------------------------------------------------------------

    public User signUp(SignUpRequest signUpRequest) throws MessagingException {
        User user = new User();

        user.setEmail(signUpRequest.getEmail());
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        user.setCreationDate(new Date());
        user.setUsername(signUpRequest.getUsername());

        User newUser= userRepository.save(user);

       // emailVerificationService.sendVerificationCode(user.getEmail(),1);

        return newUser;

    }


    private final JwtService jwtService;


    //------------------------------------------------------------------------------------------------------------------------------
    // SIGN IN (IMPLEMENTING MFA)
    //------------------------------------------------------------------------------------------------------------------------------

    public JwtAuthResponse signIn(SignInRequest signInRequest) throws MessagingException {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(),signInRequest.getPassword()));

        var user =userRepository.findByEmail(signInRequest.getEmail()).orElseThrow(()->new IllegalArgumentException("Invalid email or password"));



            var jwt =jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(new HashMap<>(),user);

            JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
            jwtAuthResponse.setToken(jwt);
            jwtAuthResponse.setRefreshToken(refreshToken);
            jwtAuthResponse.setMessage("User successfully Authenticated");
            return jwtAuthResponse;




    }





    //------------------------------------------------------------------------------------------------------------------------------
    // REFRESH JWT TOKEN
    //------------------------------------------------------------------------------------------------------------------------------

    public JwtAuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest){
        String userEmail= jwtService.extractUsername(refreshTokenRequest.getToken());

        User user =userRepository.findByEmail(userEmail).orElseThrow();

        if (jwtService.isTokenValid(refreshTokenRequest.getToken(),user)){
            var jwt =jwtService.generateToken(user);


            JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
            jwtAuthResponse.setToken(jwt);
            jwtAuthResponse.setRefreshToken(refreshTokenRequest.getToken());
            return jwtAuthResponse;

        }

        return null;
    }



    private Date determineExpirationDate() {
        Calendar calendar = Calendar.getInstance(); // Gets a calendar using the default time zone and locale.
        calendar.add(Calendar.MINUTE, 15); // Adds 15 minutes to the current time.
        return calendar.getTime(); // Returns a Date representing the new time.
    }


    private boolean isMoreThanFifteenMinutesPassed(Date expirationDate) {
        LocalDateTime expirationDateTime = convertToLocalDateTime(expirationDate);
        LocalDateTime currentDateTime = LocalDateTime.now();

        return ChronoUnit.MINUTES.between(expirationDateTime, currentDateTime) > 15;
    }

    private LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
