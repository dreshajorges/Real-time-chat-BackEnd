package com.example.realtimechat.services.serviceImpl;

import com.example.realtimechat.entities.UserEntity;
import com.example.realtimechat.repositories.UserRepository;
import com.example.realtimechat.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity create(UserEntity newUser) {
        return userRepository.save(newUser);
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }

    @Override
    public UserEntity update(Long id, UserEntity newUser) {
        return null;
        // TODO update
    }
}
