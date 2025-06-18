package com.example.realtimechat.controllers;

import com.example.realtimechat.entities.Message;
import com.example.realtimechat.entities.enums.MessageType;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.Instant;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Public chat: sendMessage → broadcast on /topic/public
     */
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public Message sendMessage(@Payload Message chatMessage) {
        chatMessage.setType(MessageType.CHAT);
        chatMessage.setTimestamp(Instant.now().toEpochMilli());
        return chatMessage;
    }

    /**
     * When a user “joins”, just broadcast a JOIN type on /topic/public.
     * We no longer store anything in sessionAttributes, avoiding the NPE.
     */
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public Message addUser(@Payload Message chatMessage) {
        chatMessage.setType(MessageType.JOIN);
        chatMessage.setTimestamp(Instant.now().toEpochMilli());
        return chatMessage;
    }

    /**
     * Private message: send to a single user’s queue.
     */
    @MessageMapping("/chat.privateMessage")
    public void sendPrivateMessage(@Payload Message chatMessage) {
        chatMessage.setType(MessageType.PRIVATE);
        chatMessage.setTimestamp(Instant.now().toEpochMilli());
        messagingTemplate.convertAndSendToUser(
                chatMessage.getTo(),
                "/queue/private",
                chatMessage
        );
    }
}
