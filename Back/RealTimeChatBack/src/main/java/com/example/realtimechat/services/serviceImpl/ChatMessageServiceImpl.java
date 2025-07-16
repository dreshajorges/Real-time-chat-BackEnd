package com.example.realtimechat.services.serviceImpl;

import com.example.realtimechat.entities.ChatMessageEntity;
import com.example.realtimechat.repositories.ChatMessageRepository;
import com.example.realtimechat.services.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;

    @Override
    public ChatMessageEntity save(ChatMessageEntity msg) {
        return chatMessageRepository.save(msg);
    }

    @Override
    public List<ChatMessageEntity> getHistory(String me, String friend) {
        return chatMessageRepository.findByFromUserAndToUserOrFromUserAndToUserOrderByTimestampAsc(
                me, friend, friend, me
        );
    }
}
