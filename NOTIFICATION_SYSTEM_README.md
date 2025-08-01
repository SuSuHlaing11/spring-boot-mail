# Real-Time Notification System

## Overview

This real-time notification system provides instant notifications for volunteer and team applications. When a user submits an application, the system automatically creates a notification and sends it to the organization's notification page in real-time via WebSocket.

## Features

- **Real-time notifications** via WebSocket (STOMP over SockJS)
- **Firebase Firestore** database storage
- **REST API** endpoints for notification management
- **Frontend components** for displaying notifications
- **Automatic notification creation** on application submission
- **Notification filtering** (all, unread, volunteer, team)
- **Mark as read/unread** functionality
- **Notification count** tracking

## Architecture

### Backend Components

1. **Notification Model** (`Notification.java`)
   - Maps to Firestore document structure
   - Contains: id, title, content, read, timestamp, email

2. **Notification Repository** (`NotificationRepository.java`)
   - Reactive Firestore repository
   - Custom query methods for filtering

3. **Notification Service** (`NotificationService.java`)
   - Business logic for notification operations
   - WebSocket messaging integration
   - CRUD operations

4. **Notification Controller** (`NotificationController.java`)
   - REST API endpoints
   - Cross-origin support
   - Health check endpoint

5. **WebSocket Configuration** (`WebsocketConfiguration.java`)
   - STOMP message broker setup
   - `/notifications` topic configuration

### Frontend Components

1. **Notification API Service** (`notifications.js`)
   - REST API client
   - Fallback to dummy data
   - Error handling

2. **WebSocket Service** (`websocket.js`)
   - STOMP client connection
   - Real-time notification subscription
   - Connection management

3. **Custom Hook** (`useNotifications.js`)
   - React hook for notification state
   - WebSocket integration
   - CRUD operations

4. **UI Components**
   - `NotificationItem.jsx` - Individual notification display
   - `NotificationList.jsx` - Notification list with filtering
   - `NotificationBell.jsx` - Header notification bell

## Database Schema

```json
{
  "notifications": {
    "{notificationId}": {
      "title": "volunteer|team",
      "content": "Full notification message",
      "read": false,
      "timestamp": "Firestore timestamp",
      "email": "recipient@email.com"
    }
  }
}
```

## API Endpoints

### Create Notifications
- `POST /api/notifications/volunteer-application`
- `POST /api/notifications/team-application`

### Retrieve Notifications
- `GET /api/notifications/{email}` - Get all notifications
- `GET /api/notifications/{email}/unread` - Get unread notifications
- `GET /api/notifications/{email}/count` - Get total count
- `GET /api/notifications/{email}/unread-count` - Get unread count

### Update Notifications
- `PUT /api/notifications/{notificationId}/read` - Mark as read
- `PUT /api/notifications/{email}/read-all` - Mark all as read

### Delete Notifications
- `DELETE /api/notifications/{notificationId}` - Delete notification

### Health Check
- `GET /api/notifications/health` - Service health status

## Notification Types

### Volunteer Application
- **Title**: "volunteer"
- **Content**: "{volunteer name} is applied for {post name}"
- **Example**: "John Doe is applied for Tree Planting Initiative"

### Team Application
- **Title**: "team"
- **Content**: "{Team name} is applied for {post name} with (members) team member."
- **Example**: "Team Green is applied for Beach Cleanup Project with (3) team member."

## Real-Time Features

### WebSocket Topics
- `/topic/notifications/{email}` - Organization-specific notifications

### Frontend Integration
- Automatic WebSocket connection on component mount
- Real-time notification updates
- Connection status management
- Automatic reconnection handling

## Usage

### Backend Setup

1. **Dependencies** (already in `pom.xml`):
   ```xml
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-websocket</artifactId>
   </dependency>
   <dependency>
       <groupId>com.google.cloud</groupId>
       <artifactId>spring-cloud-gcp-starter-data-firestore</artifactId>
   </dependency>
   ```

2. **Configuration**:
   - Firebase service account key in `firebase-service-account.json`
   - WebSocket endpoint: `ws://localhost:8080/chat`

### Frontend Setup

1. **Dependencies** (already in `package.json`):
   ```json
   {
     "sockjs-client": "^1.6.1",
     "@stomp/stompjs": "^7.0.0"
   }
   ```

2. **Usage in Components**:
   ```javascript
   import { useNotifications } from '../hooks/useNotifications';
   
   const { notifications, unreadCount, markAsRead } = useNotifications(email);
   ```

### Testing

1. **Backend Test Script**:
   ```bash
   chmod +x test-notifications.sh
   ./test-notifications.sh
   ```

2. **Frontend Test Page**:
   - Open `test-notifications.html` in browser
   - Test all endpoints interactively

## Integration Points

### Application Submission
The notification system integrates with the application form:

```javascript
// In ApplicationForm.jsx
await notificationsAPI.createVolunteerApplication(
  data.fullName,
  org.name,
  data.email
);
```

### Organization Dashboard
Notifications appear in the organization's notification page:

```javascript
// In Notifications.jsx
const { notifications, unreadCount } = useNotifications(organizationEmail);
```

### Header Notification Bell
Real-time notification count in the header:

```javascript
// In OrganizationLayout.jsx
<NotificationBell
  notifications={notifications}
  unreadCount={unreadCount}
  onMarkAsRead={markAsRead}
  onDelete={deleteNotification}
/>
```

## Configuration

### Environment Variables
- `GOOGLE_APPLICATION_CREDENTIALS` - Firebase service account path
- `spring.cloud.gcp.firestore.project-id` - Firebase project ID

### WebSocket Configuration
- Endpoint: `/chat`
- Message broker: `/topic`, `/queue`, `/notifications`
- Application prefix: `/app`

## Error Handling

### Backend
- Graceful fallback for Firestore connection issues
- WebSocket connection error handling
- REST API error responses

### Frontend
- API fallback to dummy data
- WebSocket reconnection logic
- Component error boundaries

## Performance Considerations

- **Reactive programming** with Spring WebFlux
- **Lazy loading** of notifications
- **Pagination** support (can be extended)
- **Connection pooling** for WebSocket
- **Caching** for frequently accessed data

## Security

- **CORS** configuration for cross-origin requests
- **Input validation** on all endpoints
- **Email-based** notification filtering
- **Firebase security rules** (configure separately)

## Monitoring

- **Health check** endpoint
- **WebSocket connection** status
- **Notification delivery** tracking
- **Error logging** and monitoring

## Future Enhancements

- **Push notifications** for mobile
- **Email notifications** integration
- **Notification templates** system
- **Advanced filtering** and search
- **Notification preferences** per user
- **Bulk operations** for notifications
- **Analytics** and reporting
- **Multi-language** support

## Troubleshooting

### Common Issues

1. **WebSocket Connection Failed**
   - Check if backend is running on port 8080
   - Verify CORS configuration
   - Check browser console for errors

2. **Firebase Connection Issues**
   - Verify service account key file
   - Check Firebase project configuration
   - Ensure proper permissions

3. **Notifications Not Appearing**
   - Check email address matching
   - Verify WebSocket subscription
   - Check browser network tab

### Debug Mode

Enable debug logging in `application.properties`:
```properties
logging.level.com.javacodeex=DEBUG
logging.level.org.springframework.web=DEBUG
```

## Support

For issues or questions:
1. Check the test scripts for API functionality
2. Review browser console for frontend errors
3. Check Spring Boot logs for backend issues
4. Verify Firebase configuration and permissions 