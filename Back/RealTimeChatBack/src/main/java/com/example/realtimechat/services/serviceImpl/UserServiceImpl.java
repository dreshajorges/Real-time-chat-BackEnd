// src/main/java/com/example/realtimechat/services/serviceImpl/UserServiceImpl.java
package com.example.realtimechat.services.serviceImpl;

import com.example.realtimechat.entities.UserEntity;
import com.example.realtimechat.infrastructure.exceptions.NotFoundException;
import com.example.realtimechat.repositories.UserRepository;
import com.example.realtimechat.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
        UserEntity existing = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        existing.setEmail(newUser.getEmail());
        existing.setPassword(passwordEncoder.encode(newUser.getPassword()));
        return userRepository.save(existing);
    }

    @Override
    @Transactional
    public List<UserEntity> findFriendsByEmail(String myEmail) {
        UserEntity me = userRepository.findByEmail(myEmail)
                .orElseThrow(() -> new NotFoundException("User not found"));
        // return a copy so we don’t accidentally serialize the JPA Set
        return List.copyOf(me.getFriends());
    }

    @Override
    @Transactional
    public void addFriend(String myEmail, Long friendId) {
        UserEntity me = userRepository.findByEmail(myEmail)
                .orElseThrow(() -> new NotFoundException("User not found"));
        UserEntity other = userRepository.findById(friendId)
                .orElseThrow(() -> new NotFoundException("Friend not found"));
        me.getFriends().add(other);
        userRepository.save(me);
    }

    @Override
    public List<UserEntity> searchByEmail(String q, String myEmail) {
        // 1) get all matching email fragments
        List<UserEntity> matches = userRepository.findByEmailContainingIgnoreCase(q);

        // 2) load me + my friends
        UserEntity me = userRepository.findByEmail(myEmail)
                .orElseThrow(() -> new NotFoundException("User not found"));
        var friendSet = me.getFriends();

        // 3) filter out myself & existing friends
        return matches.stream()
                .filter(u -> !u.getEmail().equalsIgnoreCase(myEmail))
                .filter(u -> !friendSet.contains(u))
                .collect(Collectors.toList());
    }
}
