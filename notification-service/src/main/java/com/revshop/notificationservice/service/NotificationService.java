package com.revshop.notificationservice.service;

import com.revshop.notificationservice.dto.NotificationRequest;
import com.revshop.notificationservice.dto.NotificationResponse;
import com.revshop.notificationservice.entity.Notification;
import com.revshop.notificationservice.enums.NotificationType;
import com.revshop.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationResponse createNotification(NotificationRequest request) {
        Notification notification = new Notification();
        notification.setUserId(request.getUserId());
        notification.setMessage(request.getMessage());
        notification.setType(NotificationType.valueOf(request.getType().toUpperCase()));
        notification.setRead(false);
        
        Notification savedNotification = notificationRepository.save(notification);
        return convertToNotificationResponse(savedNotification);
    }
    
    public List<NotificationResponse> getNotificationsByUserId(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return notifications.stream()
                .map(this::convertToNotificationResponse)
                .toList();
    }
    
    public NotificationResponse markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found with ID: " + notificationId));
        notification.setRead(true);
        
        Notification savedNotification = notificationRepository.save(notification);
        return convertToNotificationResponse(savedNotification);
    }
    
    public void markAllAsRead(Long userId) {
        List<Notification> unreadNotifications = notificationRepository.findByUserIdAndIsReadFalse(userId);
        unreadNotifications.forEach(notification -> notification.setRead(true));
        notificationRepository.saveAll(unreadNotifications);
    }
    
    public List<NotificationResponse> getAllNotifications() {
        List<Notification> notifications = notificationRepository.findAll();
        return notifications.stream()
                .map(this::convertToNotificationResponse)
                .toList();
    }
    
    public long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }
    
    private NotificationResponse convertToNotificationResponse(Notification notification) {
        return new NotificationResponse(
            notification.getNotificationId(),
            notification.getUserId(),
            notification.getMessage(),
            notification.getType().toString(),
            notification.isRead(),
            notification.getCreatedAt()
        );
    }
}
