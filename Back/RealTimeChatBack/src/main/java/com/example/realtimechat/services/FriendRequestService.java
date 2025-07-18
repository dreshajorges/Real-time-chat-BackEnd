package com.example.realtimechat.services;

import com.example.realtimechat.entities.FriendRequest;

import java.util.List;

public interface FriendRequestService {

    void sendRequest (String requesterEmail, Long recipientId);

    List<FriendRequest> getPendingRequests(String recipientEmail);

    void acceptRequest(Long requestId, String recipientEmail);

    void declineRequest(Long requestId, String recipientEmail);
}
