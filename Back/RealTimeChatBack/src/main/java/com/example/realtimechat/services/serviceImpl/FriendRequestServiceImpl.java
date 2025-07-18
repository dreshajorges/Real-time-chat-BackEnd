package com.example.realtimechat.services.serviceImpl;

import com.example.realtimechat.entities.FriendRequest;
import com.example.realtimechat.entities.UserEntity;
import com.example.realtimechat.entities.enums.RequestStatus;
import com.example.realtimechat.infrastructure.exceptions.NotFoundException;
import com.example.realtimechat.repositories.FriendRequestRepository;
import com.example.realtimechat.repositories.UserRepository;
import com.example.realtimechat.services.FriendRequestService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendRequestServiceImpl implements FriendRequestService {
    private final UserRepository userRepository;
    private final FriendRequestRepository friendRequestRepository;

    public FriendRequestServiceImpl(UserRepository userRepository, FriendRequestRepository friendRequestRepository) {
        this.userRepository = userRepository;
        this.friendRequestRepository = friendRequestRepository;
    }

    @Override
    public void sendRequest(String requesterEmail, Long recipientId) {
        UserEntity requester = userRepository.findByEmail(requesterEmail)
                .orElseThrow(() -> new NotFoundException("Requester not found"));
        UserEntity recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new NotFoundException("Recipient not found"));

        // optionally: check for existing friendship or pending request
        var fr = FriendRequest.builder()
                .requester(requester)
                .recipient(recipient)
                .status(RequestStatus.PENDING)
                .build();
        friendRequestRepository.save(fr);
    }

    @Override
    public List<FriendRequest> getPendingRequests(String recipientEmail) {
        return friendRequestRepository.findByRecipientEmailAndStatus(recipientEmail, RequestStatus.PENDING); //i lyp dy parametra masi osht findByRecipientEmailOrStatus
    }

    @Override
    @Transactional
    public void acceptRequest(Long requestId, String recipientEmail) {
        FriendRequest fr = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request not found"));
        if (!fr.getRecipient().getEmail().equals(recipientEmail)
                || fr.getStatus() != RequestStatus.PENDING) {
            throw new IllegalArgumentException("Cannot accept this request");
        }

        fr.setStatus(RequestStatus.ACCEPTED);

        //mi bo dosta
        UserEntity a = fr.getRequester();
        UserEntity b = fr.getRecipient();
        a.getFriends().add(b);
        b.getFriends().add(a);

        userRepository.save(a);
        userRepository.save(b); //pse mi bo save?
    }

    @Override
    @Transactional
    public void declineRequest(Long requestId, String recipientEmail) {
        FriendRequest fr = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request not found"));
        if (!fr.getRecipient().getEmail().equals(recipientEmail)
                || fr.getStatus() != RequestStatus.PENDING) {
            throw new IllegalArgumentException("Cannot decline this request");
        }
        fr.setStatus(RequestStatus.DECLINED);
        friendRequestRepository.save(fr);
    }
}
