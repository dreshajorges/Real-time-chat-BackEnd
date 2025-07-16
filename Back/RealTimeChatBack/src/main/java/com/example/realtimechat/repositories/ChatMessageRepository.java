package com.example.realtimechat.repositories;

import com.example.realtimechat.entities.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {
    // all messages where (from=me AND to=friend) OR (from=friend AND to=me)
    List<ChatMessageEntity> findByFromUserAndToUserOrFromUserAndToUserOrderByTimestampAsc(
            String from1, String to1, String from2, String to2);
}
