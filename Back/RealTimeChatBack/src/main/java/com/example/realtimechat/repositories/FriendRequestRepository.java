package com.example.realtimechat.repositories;

import com.example.realtimechat.entities.FriendRequest;
import com.example.realtimechat.entities.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest,Long> {

    List<FriendRequest> findByRecipientEmailAndStatus(String email, RequestStatus status);
}
