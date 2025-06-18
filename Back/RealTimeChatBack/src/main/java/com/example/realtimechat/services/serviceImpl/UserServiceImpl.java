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

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
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
        Optional<UserEntity> existingUserOptional = userRepository.findById(id);
        if (existingUserOptional.isEmpty()){
            throw new NotFoundException("User was not found");
        }

        UserEntity existingUser = existingUserOptional.get();

        existingUser.setEmail(newUser.getEmail());

        String encodedPassword = passwordEncoder.encode(newUser.getPassword());
        existingUser.setPassword(encodedPassword);

        return userRepository.save(existingUser);
    }

    @Override
    public List<UserEntity> searchUsers(String q, String myEmail) {
        return userRepository
                .findByEmailContainingIgnoreCaseOrNameContainingIgnoreCase(q, q).stream()
                .filter(u -> !u.getUsername().equals(myEmail))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<UserEntity> findFriendsByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
        // return a List copy of the Set for JSON serialization
        return List.copyOf(user.getFriends());
    }

    @Override
    @Transactional
    public void addFriend(String myEmail, Long friendId) {
        UserEntity me     = userRepository.findByEmail(myEmail)
                .orElseThrow(() -> new NotFoundException("me not found"));
        UserEntity other  = userRepository.findById(friendId)
                .orElseThrow(() -> new NotFoundException("friend not found"));

        me.getFriends().add(other);
        userRepository.save(me);
    }


}
