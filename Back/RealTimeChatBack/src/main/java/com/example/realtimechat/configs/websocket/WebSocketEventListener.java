//package com.example.realtimechat.configs.websocket;
//
//import com.example.realtimechat.entities.ChatMessage;
//import com.example.realtimechat.entities.enums.MessageType;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.messaging.simp.SimpMessageSendingOperations;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.messaging.SessionDisconnectEvent;
//
//@Component
//@Slf4j
//@RequiredArgsConstructor
//public class WebSocketEventListener {
//
//    private final SimpMessageSendingOperations messageTemplate;
//
//    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
//        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
//        String username = (String) headerAccessor.getSessionAttributes().get("username");
//        if (username != null) {
//            log.info("User disconnected: {}", username);
//            var chatMessage = ChatMessage.builder().messageType(MessageType.LEAVE).sender(username).build();
//            messageTemplate.convertAndSend("/topic/public", chatMessage);
//        }
//    }
//}
