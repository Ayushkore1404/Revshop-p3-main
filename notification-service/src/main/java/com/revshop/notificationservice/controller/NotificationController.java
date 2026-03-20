package com.revshop.notificationservice.controller;

import com.revshop.notificationservice.dto.NotificationRequest;
import com.revshop.notificationservice.dto.NotificationResponse;
import com.revshop.notificationservice.service.NotificationService;
import com.revshop.notificationservice.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final JwtUtil jwtUtil;

    @PostMapping("/order")
    public ResponseEntity<NotificationResponse> createOrderNotification(@RequestBody NotificationRequest request) {
        NotificationResponse response = notificationService.createNotification(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/low-stock")
    public ResponseEntity<NotificationResponse> createLowStockNotification(@RequestBody NotificationRequest request) {
        NotificationResponse response = notificationService.createNotification(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('BUYER', 'SELLER', 'ADMIN')")
    public ResponseEntity<List<NotificationResponse>> getMyNotifications(
            @RequestHeader("Authorization") String authHeader) {
        
        String token = authHeader.substring(7);
        String userIdStr = jwtUtil.extractUserId(token);
        Long userId = Long.parseLong(userIdStr);
        
        List<NotificationResponse> notifications = notificationService.getNotificationsByUserId(userId);
        return ResponseEntity.ok(notifications);
    }

    @PutMapping("/{id}/read")
    @PreAuthorize("hasAnyRole('BUYER', 'SELLER', 'ADMIN')")
    public ResponseEntity<NotificationResponse> markAsRead(@PathVariable Long id) {
        NotificationResponse notification = notificationService.markAsRead(id);
        return ResponseEntity.ok(notification);
    }

    @PutMapping("/read-all")
    @PreAuthorize("hasAnyRole('BUYER', 'SELLER', 'ADMIN')")
    public ResponseEntity<Void> markAllAsRead(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String userIdStr = jwtUtil.extractUserId(token);
        Long userId = Long.parseLong(userIdStr);
        
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<NotificationResponse>> getAllNotifications() {
        List<NotificationResponse> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/unread-count")
    @PreAuthorize("hasAnyRole('BUYER', 'SELLER', 'ADMIN')")
    public ResponseEntity<Long> getUnreadCount(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String userIdStr = jwtUtil.extractUserId(token);
        Long userId = Long.parseLong(userIdStr);
        
        long count = notificationService.getUnreadCount(userId);
        return ResponseEntity.ok(count);
    }
}
