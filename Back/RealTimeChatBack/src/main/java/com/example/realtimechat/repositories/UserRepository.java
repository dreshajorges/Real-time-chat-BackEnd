package com.example.realtimechat.repositories;

import com.example.realtimechat.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity> findByEmail(String email);

    List<UserEntity> findByEmailContainingIgnoreCaseOrNameContainingIgnoreCase(String email, String name);

}
