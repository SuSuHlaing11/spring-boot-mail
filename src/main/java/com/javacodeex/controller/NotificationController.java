package com.javacodeex.controller;

import com.javacodeex.dto.NotificationRequest;
import com.javacodeex.model.Notification;
import com.javacodeex.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {
    
    @Autowired
    private NotificationService notificationService;
    
    // Create notification for volunteer application
    @PostMapping("/volunteer-application")
    public Mono<ResponseEntity<Notification>> createVolunteerApplicationNotification(
            @RequestBody NotificationRequest.VolunteerApplicationRequest request) {
        return notificationService.createVolunteerApplicationNotification(
                request.getVolunteerName(), 
                request.getPostName(), 
                request.getVolunteerEmail()
        )
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.badRequest().build());
    }
    
    // Create notification for team application
    @PostMapping("/team-application")
    public Mono<ResponseEntity<Notification>> createTeamApplicationNotification(
            @RequestBody NotificationRequest.TeamApplicationRequest request) {
        return notificationService.createTeamApplicationNotification(
                request.getTeamName(), 
                request.getPostName(), 
                request.getTeamMembers(), 
                request.getTeamEmail()
        )
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.badRequest().build());
    }
    
    // Get all notifications for an email
    @GetMapping("/{email}")
    public Flux<Notification> getNotificationsByEmail(@PathVariable String email) {
        return notificationService.getNotificationsByEmail(email);
    }
    
    // Get unread notifications for an email
    @GetMapping("/{email}/unread")
    public Flux<Notification> getUnreadNotificationsByEmail(@PathVariable String email) {
        return notificationService.getUnreadNotificationsByEmail(email);
    }
    
    // Mark notification as read
    @PutMapping("/{notificationId}/read")
    public Mono<ResponseEntity<Notification>> markAsRead(@PathVariable String notificationId) {
        return notificationService.markAsRead(notificationId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
    // Mark all notifications as read for an email
    @PutMapping("/{email}/read-all")
    public Flux<Notification> markAllAsRead(@PathVariable String email) {
        return notificationService.markAllAsRead(email);
    }
    
    // Delete notification
    @DeleteMapping("/{notificationId}")
    public Mono<ResponseEntity<Void>> deleteNotification(@PathVariable String notificationId) {
        return notificationService.deleteNotification(notificationId)
                .then(Mono.just(ResponseEntity.ok().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
    // Get notification count for an email
    @GetMapping("/{email}/count")
    public Mono<ResponseEntity<Map<String, Long>>> getNotificationCount(@PathVariable String email) {
        return notificationService.getNotificationCount(email)
                .map(count -> Map.of("count", count))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.ok(Map.of("count", 0L)));
    }
    
    // Get unread notification count for an email
    @GetMapping("/{email}/unread-count")
    public Mono<ResponseEntity<Map<String, Long>>> getUnreadNotificationCount(@PathVariable String email) {
        return notificationService.getUnreadNotificationCount(email)
                .map(count -> Map.of("unreadCount", count))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.ok(Map.of("unreadCount", 0L)));
    }
    
    // Health check endpoint
    @GetMapping("/health")
    public Mono<ResponseEntity<Map<String, String>>> health() {
        return Mono.just(ResponseEntity.ok(Map.of("status", "Notification service is running")));
    }
} 