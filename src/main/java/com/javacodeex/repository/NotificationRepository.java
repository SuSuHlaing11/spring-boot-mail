package com.javacodeex.repository;

import com.javacodeex.model.Notification;
import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface NotificationRepository extends FirestoreReactiveRepository<Notification> {

    // Find notifications by email
    Flux<Notification> findByEmail(String email);

    // Find unread notifications by email
    Flux<Notification> findByEmailAndRead(String email, boolean read);

    // Find notifications by email ordered by timestamp descending
    // NOTE: This requires a composite index in Firestore
    // Alternative: Use findByEmail() and sort in application layer
    Flux<Notification> findByEmailOrderByTimestampDesc(String email);
} 