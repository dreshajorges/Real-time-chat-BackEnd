package com.example.realtimechat.entities.dtos;

import com.example.realtimechat.entities.enums.MessageType;
import lombok.*;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    private MessageType type;
    private String from;
    private String to;         // null for broadcast
    private String content;
    private Instant timestamp; // use Instant here
}
