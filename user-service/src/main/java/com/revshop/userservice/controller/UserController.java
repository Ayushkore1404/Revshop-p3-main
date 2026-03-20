package com.revshop.userservice.controller;

import com.revshop.userservice.dto.UserResponse;
import com.revshop.userservice.dto.RegisterRequest;
import com.revshop.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @RequestBody RegisterRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        UserResponse response = userService.updateUser(id, request, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }
}
