package com.example.realtimechat.controllers;

import com.example.realtimechat.entities.FriendRequest;
import com.example.realtimechat.entities.dtos.FriendDto;
import com.example.realtimechat.entities.dtos.FriendRequestDto;
import com.example.realtimechat.services.FriendRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/chat/requests")
public class FriendRequestController {

    private final FriendRequestService friendRequestService;

    public FriendRequestController(FriendRequestService friendRequestService) {
        this.friendRequestService = friendRequestService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> sendRequest(@PathVariable("id") Long recipientId, Principal principal){

        friendRequestService.sendRequest(principal.getName(), recipientId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<FriendRequestDto>> list(Principal principal) {
        List<FriendRequest> pending = friendRequestService.getPendingRequests(principal.getName());
        var dtos = pending.stream()
                .map(fr -> new FriendRequestDto(
                        fr.getId(),
                        fr.getRequester().getId(),
                        fr.getRequester().getName(),
                        fr.getRequester().getSurname(),
                        fr.getRequester().getEmail()
                )).toList();
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<Void> accept(@PathVariable Long id, Principal principal) {
        friendRequestService.acceptRequest(id, principal.getName());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/decline")
    public ResponseEntity<Void> decline(@PathVariable Long id, Principal principal) {
        friendRequestService.declineRequest(id, principal.getName());
        return ResponseEntity.ok().build();
    }
}

//TODO : me i kuptu stream map per DTO