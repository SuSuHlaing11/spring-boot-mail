package com.javacodeex.service;

import com.google.cloud.Timestamp;
import com.javacodeex.model.Notification;
import com.javacodeex.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service  // Commented out to use MockNotificationService instead
public class NotificationService {
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    // Create and send notification for volunteer application
    public Mono<Notification> createVolunteerApplicationNotification(String volunteerName, String postName, String volunteerEmail) {
        String title = "volunteer";
        String content = volunteerName + " is applied for " + postName;
        String organizationEmail = "yiyao52013142@gmail.com"; // For now using simple var as requested
        
        Notification notification = new Notification(title, content, false, Timestamp.now(), organizationEmail);
        
        return notificationRepository.save(notification)
                .doOnSuccess(savedNotification -> {
                    // Send real-time notification via WebSocket
                    messagingTemplate.convertAndSend("/topic/notifications/" + organizationEmail, savedNotification);
                });
    }
    
    // Create and send notification for team application
    public Mono<Notification> createTeamApplicationNotification(String teamName, String postName, String teamMembers, String teamEmail) {
        String title = "team";
        String content = teamName + " is applied for " + postName + " with (" + teamMembers + ") team member.";
        String organizationEmail = "yiyao52013142@gmail.com"; // For now using simple var as requested
        
        Notification notification = new Notification(title, content, false, Timestamp.now(), organizationEmail);
        
        return notificationRepository.save(notification)
                .doOnSuccess(savedNotification -> {
                    // Send real-time notification via WebSocket
                    messagingTemplate.convertAndSend("/topic/notifications/" + organizationEmail, savedNotification);
                });
    }
    
    // Get all notifications for an email
    public Flux<Notification> getNotificationsByEmail(String email) {
        return notificationRepository.findByEmailOrderByTimestampDesc(email);
    }
    
    // Get unread notifications for an email
    public Flux<Notification> getUnreadNotificationsByEmail(String email) {
        return notificationRepository.findByEmailAndRead(email, false);
    }
    
    // Mark notification as read
    public Mono<Notification> markAsRead(String notificationId) {
        return notificationRepository.findById(notificationId)
                .flatMap(notification -> {
                    notification.setRead(true);
                    return notificationRepository.save(notification);
                });
    }
    
    // Mark all notifications as read for an email
    public Flux<Notification> markAllAsRead(String email) {
        return notificationRepository.findByEmailAndRead(email, false)
                .flatMap(notification -> {
                    notification.setRead(true);
                    return notificationRepository.save(notification);
                });
    }
    
    // Delete notification
    public Mono<Void> deleteNotification(String notificationId) {
        return notificationRepository.deleteById(notificationId);
    }
    
    // Get notification count for an email
    public Mono<Long> getNotificationCount(String email) {
        return notificationRepository.findByEmail(email).count();
    }
    
    // Get unread notification count for an email
    public Mono<Long> getUnreadNotificationCount(String email) {
        return notificationRepository.findByEmailAndRead(email, false).count();
    }
} 