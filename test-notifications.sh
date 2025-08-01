#!/bin/bash

# Test script for notification system
# Make sure the Spring Boot application is running on localhost:8080

echo "Testing Notification System..."
echo "=============================="

# Test health endpoint
echo "1. Testing health endpoint..."
curl -s http://localhost:8080/api/notifications/health | jq .

echo -e "\n2. Testing volunteer application notification..."
curl -s -X POST http://localhost:8080/api/notifications/volunteer-application \
  -H "Content-Type: application/json" \
  -d '{
    "volunteerName": "John Doe",
    "postName": "Tree Planting Initiative",
    "volunteerEmail": "john.doe@example.com"
  }' | jq .

echo -e "\n3. Testing team application notification..."
curl -s -X POST http://localhost:8080/api/notifications/team-application \
  -H "Content-Type: application/json" \
  -d '{
    "teamName": "Team Green",
    "postName": "Beach Cleanup Project",
    "teamMembers": "3",
    "teamEmail": "team.green@example.com"
  }' | jq .

echo -e "\n4. Testing get notifications by email..."
curl -s http://localhost:8080/api/notifications/yiyao52013142@gmail.com | jq .

echo -e "\n5. Testing get unread count..."
curl -s http://localhost:8080/api/notifications/yiyao52013142@gmail.com/unread-count | jq .

echo -e "\n6. Testing get total count..."
curl -s http://localhost:8080/api/notifications/yiyao52013142@gmail.com/count | jq .

echo -e "\nTests completed!" 