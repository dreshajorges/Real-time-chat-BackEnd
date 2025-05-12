package com.example.realtimechat.services;

import com.example.realtimechat.entities.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface UserService {


    UserEntity create (UserEntity newUser);

    Optional<UserEntity> findById(Long id);

    List<UserEntity> findAll();

    void deleteById(Long id);

    void deleteAll();

    UserEntity update (Long id, UserEntity newUser);
}
