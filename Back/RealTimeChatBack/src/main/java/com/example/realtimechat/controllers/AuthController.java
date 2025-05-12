package com.example.realtimechat.controllers;

import com.example.realtimechat.entities.dtos.UserAuthResponse;
import com.example.realtimechat.entities.dtos.UserLoginRequest;
import com.example.realtimechat.entities.dtos.UserRegisterRequest;
import com.example.realtimechat.services.serviceImpl.AuthServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/chat/auth")
public class AuthController {

    private final AuthServiceImpl authService;

    public AuthController(AuthServiceImpl authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserAuthResponse> signUp(UserRegisterRequest userRegisterRequest){
        return ResponseEntity.ok(authService.signUp(userRegisterRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<UserAuthResponse> login(UserLoginRequest userLoginRequest){
        return ResponseEntity.ok(authService.login(userLoginRequest));
    }
}
