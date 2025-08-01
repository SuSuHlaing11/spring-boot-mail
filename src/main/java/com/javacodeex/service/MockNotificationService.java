package com.javacodeex.service;

import com.google.cloud.Timestamp;
import com.javacodeex.model.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// @Service
public class MockNotificationService {

    private final Map<String, Notification> notifications = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Create and send notification for volunteer application
    public Mono<Notification> createVolunteerApplicationNotification(String volunteerName, String postName, String volunteerEmail) {
        String title = "volunteer";
        String content = volunteerName + " is applied for " + postName;
        String organizationEmail = "yiyao52013142@gmail.com"; // For now using simple var as requested

        Notification notification = new Notification(title, content, false, Timestamp.now(), organizationEmail);
        notification.setId(String.valueOf(idCounter.getAndIncrement()));
        
        notifications.put(notification.getId(), notification);

        // Send real-time notification via WebSocket
        messagingTemplate.convertAndSend("/topic/notifications/" + organizationEmail, notification);

        return Mono.just(notification);
    }

    // Create and send notification for team application
    public Mono<Notification> createTeamApplicationNotification(String teamName, String postName, String teamMembers, String teamEmail) {
        String title = "team";
        String content = teamName + " is applied for " + postName + " with (" + teamMembers + ") team member.";
        String organizationEmail = "yiyao52013142@gmail.com"; // For now using simple var as requested

        Notification notification = new Notification(title, content, false, Timestamp.now(), organizationEmail);
        notification.setId(String.valueOf(idCounter.getAndIncrement()));
        
        notifications.put(notification.getId(), notification);

        // Send real-time notification via WebSocket
        messagingTemplate.convertAndSend("/topic/notifications/" + organizationEmail, notification);

        return Mono.just(notification);
    }

    // Get all notifications for an email
    public Flux<Notification> getNotificationsByEmail(String email) {
        return Flux.fromIterable(notifications.values())
                .filter(notification -> email.equals(notification.getEmail()))
                .sort((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()));
    }

    // Get unread notifications for an email
    public Flux<Notification> getUnreadNotificationsByEmail(String email) {
        return Flux.fromIterable(notifications.values())
                .filter(notification -> email.equals(notification.getEmail()) && !notification.isRead());
    }

    // Mark notification as read
    public Mono<Notification> markAsRead(String notificationId) {
        Notification notification = notifications.get(notificationId);
        if (notification != null) {
            notification.setRead(true);
            notifications.put(notificationId, notification);
            return Mono.just(notification);
        }
        return Mono.empty();
    }

    // Mark all notifications as read for an email
    public Flux<Notification> markAllAsRead(String email) {
        return Flux.fromIterable(notifications.values())
                .filter(notification -> email.equals(notification.getEmail()) && !notification.isRead())
                .doOnNext(notification -> {
                    notification.setRead(true);
                    notifications.put(notification.getId(), notification);
                });
    }

    // Delete notification
    public Mono<Void> deleteNotification(String notificationId) {
        notifications.remove(notificationId);
        return Mono.empty();
    }

    // Get notification count for an email
    public Mono<Long> getNotificationCount(String email) {
        return Flux.fromIterable(notifications.values())
                .filter(notification -> email.equals(notification.getEmail()))
                .count();
    }

    // Get unread notification count for an email
    public Mono<Long> getUnreadNotificationCount(String email) {
        return Flux.fromIterable(notifications.values())
                .filter(notification -> email.equals(notification.getEmail()) && !notification.isRead())
                .count();
    }
} 