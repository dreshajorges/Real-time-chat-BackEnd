package com.example.realtimechat.repositories;

import com.example.realtimechat.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {
    List<Message> findBySenderOrRecipient(String sender, String recipient);
}
