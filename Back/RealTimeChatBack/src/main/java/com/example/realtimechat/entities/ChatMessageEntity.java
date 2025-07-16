package com.example.realtimechat.entities;

import com.example.realtimechat.entities.enums.MessageType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "chat_messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MessageType type;

    private String fromUser;

    private String toUser;    // null for PUBLIC messages

    // use native TEXT rather than a JDBC CLOB
    @Column(columnDefinition = "TEXT")
    private String content;

    private Instant timestamp;
}
