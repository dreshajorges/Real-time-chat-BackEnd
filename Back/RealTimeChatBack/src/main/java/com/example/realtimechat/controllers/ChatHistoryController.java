package com.example.realtimechat.controllers;

import com.example.realtimechat.entities.ChatMessageEntity;
import com.example.realtimechat.services.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatHistoryController {

    private final ChatMessageService messageService;

    @GetMapping("/history/{friendEmail}")
    public ResponseEntity<List<ChatMessageEntity>> getHistory(
            @PathVariable String friendEmail,
            Principal principal
    ) {
        String me = principal.getName();
        List<ChatMessageEntity> history = messageService.getHistory(me, friendEmail);
        return ResponseEntity.ok(history);
    }
}
