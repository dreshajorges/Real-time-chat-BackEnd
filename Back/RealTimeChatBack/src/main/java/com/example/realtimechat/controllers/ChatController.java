package com.example.realtimechat.controllers;

import com.example.realtimechat.entities.ChatMessageEntity;
import com.example.realtimechat.entities.dtos.Message;
import com.example.realtimechat.entities.enums.MessageType;
import com.example.realtimechat.services.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.Instant;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService messageService;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public Message sendMessage(@Payload Message chatMessage) {
        chatMessage.setType(MessageType.CHAT);
        // Pass an Instant directly:
        chatMessage.setTimestamp(Instant.now());

        // Persist the public message
        messageService.save(
                ChatMessageEntity.builder()
                        .type(MessageType.CHAT)
                        .fromUser(chatMessage.getFrom())
                        // toUser null for broadcasts
                        .toUser(null)
                        .content(chatMessage.getContent())
                        // use the Instant directly
                        .timestamp(chatMessage.getTimestamp())
                        .build()
        );

        return chatMessage;
    }
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public Message addUser(@Payload Message chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getFrom());
        chatMessage.setType(MessageType.JOIN);
        chatMessage.setTimestamp(chatMessage.getTimestamp());
        return chatMessage;
    }

    @MessageMapping("/chat.privateMessage")
    public void sendPrivateMessage(@Payload Message chatMessage) {
        chatMessage.setType(MessageType.PRIVATE);
        chatMessage.setTimestamp(Instant.now());

        // Persist the private message
        messageService.save(
                ChatMessageEntity.builder()
                        .type(MessageType.PRIVATE)
                        .fromUser(chatMessage.getFrom())
                        .toUser(chatMessage.getTo())
                        .content(chatMessage.getContent())
                        .timestamp(chatMessage.getTimestamp())
                        .build()
        );

        // Send it over STOMP
        messagingTemplate.convertAndSendToUser(
                chatMessage.getTo(),
                "/queue/private",
                chatMessage
        );
    }
}
