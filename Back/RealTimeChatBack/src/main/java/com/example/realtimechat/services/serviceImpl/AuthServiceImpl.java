package com.example.realtimechat.services.serviceImpl;

import com.example.realtimechat.configs.authentication.JwtService;
import com.example.realtimechat.entities.UserEntity;
import com.example.realtimechat.entities.dtos.UserAuthResponse;
import com.example.realtimechat.entities.dtos.UserLoginRequest;
import com.example.realtimechat.entities.dtos.UserRegisterRequest;
import com.example.realtimechat.entities.enums.Roles;
import com.example.realtimechat.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public UserAuthResponse signUp(UserRegisterRequest userRegisterRequest) {

        boolean isFirstUser = userRepository.count() == 0;

        Roles role = isFirstUser ? Roles.ADMIN : Roles.USER;

        var user = UserEntity.builder()
                .name(userRegisterRequest.getName())
                .surname(userRegisterRequest.getSurname())
                .birthdate(userRegisterRequest.getBirthdate())
                .gender(userRegisterRequest.getGender())
                .email(userRegisterRequest.getEmail())
                .password(passwordEncoder.encode(userRegisterRequest.getPassword()))
                .role(role)
                .build();

        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return UserAuthResponse.builder().token(jwtToken).build();
    }

    public UserAuthResponse login(UserLoginRequest userLoginRequest) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        userLoginRequest.getEmail(),
                        userLoginRequest.getPassword()
                ));

                var user = userRepository.findByEmail(userLoginRequest.getEmail()).orElseThrow();

                var jwtToken = jwtService.generateToken(user);
                return UserAuthResponse.builder()
                        .token(jwtToken)
                        .build();
    }
}
