package com.example.realtimechat.services;

import com.example.realtimechat.entities.ChatMessageEntity;

import java.util.List;

public interface ChatMessageService {
    ChatMessageEntity save(ChatMessageEntity msg);
    List<ChatMessageEntity> getHistory(String me, String friend);
}
