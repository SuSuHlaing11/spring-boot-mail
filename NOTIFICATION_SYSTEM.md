# Real-Time Notification System

This document describes the real-time notification system implemented for volunteer and team applications.

## Overview

The notification system provides real-time notifications when volunteers or teams submit applications. Notifications are stored in Firebase Firestore and sent via WebSocket for instant delivery.

## Architecture

### Backend (Spring Boot)

#### Components:
1. **Notification Model** (`Notification.java`)
   - Represents notification data structure
   - Fields: id, title, content, read, timestamp, email

2. **Notification Repository** (`NotificationRepository.java`)
   - Firebase Firestore repository for data persistence
   - Provides methods for CRUD operations

3. **Notification Service** (`NotificationService.java`)
   - Business logic for notification operations
   - Handles WebSocket messaging
   - Creates notifications for volunteer and team applications

4. **Notification Controller** (`NotificationController.java`)
   - REST API endpoints for notification management
   - Handles HTTP requests for CRUD operations

5. **WebSocket Configuration** (`WebsocketConfiguration.java`)
   - Configures WebSocket endpoints for real-time communication
   - Enables `/notifications` topic for notification delivery

#### API Endpoints:

```
POST /api/notifications/volunteer-application
POST /api/notifications/team-application
GET /api/notifications/{email}
GET /api/notifications/{email}/unread
PUT /api/notifications/{notificationId}/read
PUT /api/notifications/{email}/read-all
DELETE /api/notifications/{notificationId}
GET /api/notifications/{email}/count
GET /api/notifications/{email}/unread-count
GET /api/notifications/health
```

### Frontend (React)

#### Components:
1. **NotificationBell** (`NotificationBell.jsx`)
   - Header notification bell with dropdown
   - Shows unread count badge
   - Displays recent notifications

2. **NotificationList** (`NotificationList.jsx`)
   - Full notification management interface
   - Filtering by type (all, unread, volunteer, team)
   - Bulk actions (mark all as read)

3. **NotificationItem** (`NotificationItem.jsx`)
   - Individual notification display
   - Read/unread status indicators
   - Action buttons (mark as read, delete)

#### Services:
1. **notificationsAPI** (`notifications.js`)
   - API client for notification operations
   - Handles HTTP requests to backend
   - Provides fallback dummy data

2. **webSocketService** (`websocket.js`)
   - WebSocket client for real-time updates
   - Connects to Spring Boot WebSocket endpoint
   - Handles notification subscriptions

3. **useNotifications Hook** (`useNotifications.js`)
   - React hook for notification state management
   - Integrates API and WebSocket services
   - Provides notification operations

## Database Schema

### Firebase Firestore Collection: `notifications`

```json
{
  "id": "string",
  "title": "volunteer" | "team",
  "content": "string",
  "read": "boolean",
  "timestamp": "timestamp",
  "email": "string"
}
```

## Notification Types

### Volunteer Application
- **Title**: "volunteer"
- **Content**: "{volunteerName} is applied for {postName}"
- **Email**: Organization email (yiyao52013142@gmail.com)

### Team Application
- **Title**: "team"
- **Content**: "{teamName} is applied for {postName} with ({teamMembers}) team member."
- **Email**: Organization email (yiyao52013142@gmail.com)

## Real-Time Features

### WebSocket Integration
- Notifications are sent immediately via WebSocket
- Frontend receives real-time updates
- No page refresh required

### Automatic Updates
- Notification count updates in real-time
- Unread badges update instantly
- New notifications appear in dropdown

## Usage

### Creating Notifications

When a volunteer submits an application:

```javascript
// In ApplicationForm.jsx
await notificationsAPI.createVolunteerApplication(
  data.fullName,
  org.name,
  data.email
);
```

When a team submits an application:

```javascript
// In ApplicationForm.jsx
await notificationsAPI.createTeamApplication(
  data.teamName,
  org.name,
  data.members,
  data.leaderEmail
);
```

### Displaying Notifications

In organization dashboard:

```javascript
// In OrganizationLayout.jsx
const {
  notifications,
  unreadCount,
  markAsRead,
  deleteNotification
} = useNotifications(organizationEmail);

<NotificationBell
  notifications={notifications}
  unreadCount={unreadCount}
  onMarkAsRead={markAsRead}
  onDelete={deleteNotification}
/>
```

## Configuration

### Backend Configuration

1. **Firebase Configuration** (`application.properties`):
```properties
firebase.project.id=${FIREBASE_PROJECT_ID:your-project-id}
firebase.credentials.path=${FIREBASE_CREDENTIALS_PATH:classpath:firebase-service-account.json}
```

2. **WebSocket Configuration**:
   - Endpoint: `/chat`
   - Topics: `/topic/notifications/{email}`

### Frontend Configuration

1. **API Base URL** (`axios.js`):
```javascript
baseURL: 'http://localhost:8080'
```

2. **WebSocket URL** (`websocket.js`):
```javascript
const socket = new SockJS('http://localhost:8080/chat');
```

## Dependencies

### Backend Dependencies
- Spring Boot WebSocket
- Spring Data Firestore
- Firebase Admin SDK
- Reactor Core

### Frontend Dependencies
- SockJS
- @stomp/stompjs
- React Router
- Lucide React (icons)

## Testing

### Manual Testing
1. Submit a volunteer application
2. Check organization notification page
3. Verify real-time notification appears
4. Test mark as read functionality
5. Test notification filtering

### API Testing
```bash
# Test volunteer application notification
curl -X POST http://localhost:8080/api/notifications/volunteer-application \
  -H "Content-Type: application/json" \
  -d '{"volunteerName":"John Doe","postName":"Tree Planting","volunteerEmail":"john@example.com"}'

# Test team application notification
curl -X POST http://localhost:8080/api/notifications/team-application \
  -H "Content-Type: application/json" \
  -d '{"teamName":"Team Green","postName":"Beach Cleanup","teamMembers":"3","teamEmail":"team@example.com"}'

# Get notifications
curl http://localhost:8080/api/notifications/yiyao52013142@gmail.com
```

## Future Enhancements

1. **Email Integration**: Send email notifications alongside real-time notifications
2. **Push Notifications**: Implement browser push notifications
3. **Notification Preferences**: Allow users to configure notification settings
4. **Rich Notifications**: Support for images and action buttons
5. **Notification History**: Archive old notifications
6. **Multi-language Support**: Internationalize notification messages 