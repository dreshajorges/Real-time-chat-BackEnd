package com.example.realtimechat.entities.dtos;

public record FriendRequestDto (
    Long requestId,
    Long requesterId,
    String name,
    String surname,
    String email
){}
