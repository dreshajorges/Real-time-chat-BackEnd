package com.example.realtimechat.controllers;

import com.example.realtimechat.entities.UserEntity;
import com.example.realtimechat.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/chat/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/friends")
    public ResponseEntity<List<UserEntity>> getMyFriends(Principal principal) {
        String me = principal.getName();
        List<UserEntity> friends = userService.findFriendsByEmail(me);
        return ResponseEntity.ok(friends);
    }

    @PostMapping("/{id}/friends")
    public ResponseEntity<Void> addFriend(
            @PathVariable("id") Long friendId,
            Principal principal) {

        userService.addFriend(principal.getName(), friendId);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Optional<UserEntity>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));

    }

    @GetMapping
    public ResponseEntity<List<UserEntity>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    // in UserController.java
    @GetMapping("/search")
    public ResponseEntity<List<UserEntity>> searchUsers(
            @RequestParam("q") String query,
            Principal principal) {
        // delegate to your service; filter out the caller themself
        List<UserEntity> matches = userService.searchUsers(query, principal.getName());
        return ResponseEntity.ok(matches);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.ok("User was deleted");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAll() {
        userService.deleteAll();
        return ResponseEntity.ok("All users were deleted");

    }

    @PutMapping("/{id}")
    public ResponseEntity<UserEntity> update(@PathVariable Long id, @RequestBody UserEntity user) {
        return ResponseEntity.ok(userService.update(id, user));
    }


}
