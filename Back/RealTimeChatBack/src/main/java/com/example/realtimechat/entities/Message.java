package com.example.realtimechat.entities;

import com.example.realtimechat.entities.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    private MessageType type;
    private String from;
    private String to;
    private String content;
    private long timestamp;
}
