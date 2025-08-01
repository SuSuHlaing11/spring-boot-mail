package com.javacodeex.model;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.spring.data.firestore.Document;

@Document(collectionName = "notifications")
public class Notification {
    
    @DocumentId
    private String id;
    
    private String title;
    private String content;
    private boolean read;
    private Timestamp timestamp;
    private String email;
    
    // Default constructor
    public Notification() {}
    
    // Constructor with all fields
    public Notification(String title, String content, boolean read, Timestamp timestamp, String email) {
        this.title = title;
        this.content = content;
        this.read = read;
        this.timestamp = timestamp;
        this.email = email;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public boolean isRead() {
        return read;
    }
    
    public void setRead(boolean read) {
        this.read = read;
    }
    
    public Timestamp getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    @Override
    public String toString() {
        return "Notification{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", read=" + read +
                ", timestamp=" + timestamp +
                ", email='" + email + '\'' +
                '}';
    }
} 