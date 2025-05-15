package com.example.realtimechat.controllers;

import com.example.realtimechat.entities.dtos.UserAuthResponse;
import com.example.realtimechat.entities.dtos.UserLoginRequest;
import com.example.realtimechat.entities.dtos.UserRegisterRequest;
import com.example.realtimechat.services.serviceImpl.AuthServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/chat/auth")
public class AuthController {

    private final AuthServiceImpl authService;

    public AuthController(AuthServiceImpl authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserAuthResponse> signUp(@RequestBody UserRegisterRequest userRegisterRequest){
        return ResponseEntity.ok(authService.signUp(userRegisterRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<UserAuthResponse> login(@RequestBody UserLoginRequest userLoginRequest){
        return ResponseEntity.ok(authService.login(userLoginRequest));
    }
}

// "email": "john.doe@example.com",
//  "password": "securePassword123"
