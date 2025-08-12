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
import java.util.Map;
import java.util.Optional;

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

    /**
     * MERGE‐style PUT: only overwrite fields when new values are non-null.
     */
    @Override
    public UserEntity update(Long id, UserEntity newUser) {
        UserEntity existing = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        // update email if provided
        if (newUser.getEmail() != null) {
            existing.setEmail(newUser.getEmail());
        }

        // update password only if non-null and non-blank
        String rawPw = newUser.getPassword();
        if (rawPw != null && !rawPw.isBlank()) {
            existing.setPassword(passwordEncoder.encode(rawPw));
        }

        return userRepository.save(existing);
    }

    @Override
    @Transactional
    public List<UserEntity> findFriendsByEmail(String myEmail) {
        UserEntity me = userRepository.findByEmail(myEmail)
                .orElseThrow(() -> new NotFoundException("User not found"));
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
        List<UserEntity> matches = userRepository.findByEmailContainingIgnoreCase(q);
        UserEntity me = userRepository.findByEmail(myEmail)
                .orElseThrow(() -> new NotFoundException("User not found"));
        var friendSet = me.getFriends();
        return matches.stream()
                .filter(u -> !u.getEmail().equalsIgnoreCase(myEmail))
                .filter(u -> !friendSet.contains(u))
                .toList();
    }

    @Override
    public UserEntity patchUpdate(Long id, Map<String, Object> updates) {
        UserEntity existing = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (updates.containsKey("email")) {
            existing.setEmail((String) updates.get("email"));
        }
        if (updates.containsKey("password")) {
            String raw = (String) updates.get("password");
            if (raw != null && !raw.isBlank()) {
                existing.setPassword(passwordEncoder.encode(raw));
            }
        }
        return userRepository.save(existing);
    }
}
