package com.javacodeex.dto;

public class NotificationRequest {
    
    public static class VolunteerApplicationRequest {
        private String volunteerName;
        private String postName;
        private String volunteerEmail;
        
        public VolunteerApplicationRequest() {}
        
        public VolunteerApplicationRequest(String volunteerName, String postName, String volunteerEmail) {
            this.volunteerName = volunteerName;
            this.postName = postName;
            this.volunteerEmail = volunteerEmail;
        }
        
        public String getVolunteerName() {
            return volunteerName;
        }
        
        public void setVolunteerName(String volunteerName) {
            this.volunteerName = volunteerName;
        }
        
        public String getPostName() {
            return postName;
        }
        
        public void setPostName(String postName) {
            this.postName = postName;
        }
        
        public String getVolunteerEmail() {
            return volunteerEmail;
        }
        
        public void setVolunteerEmail(String volunteerEmail) {
            this.volunteerEmail = volunteerEmail;
        }
    }
    
    public static class TeamApplicationRequest {
        private String teamName;
        private String postName;
        private String teamMembers;
        private String teamEmail;
        
        public TeamApplicationRequest() {}
        
        public TeamApplicationRequest(String teamName, String postName, String teamMembers, String teamEmail) {
            this.teamName = teamName;
            this.postName = postName;
            this.teamMembers = teamMembers;
            this.teamEmail = teamEmail;
        }
        
        public String getTeamName() {
            return teamName;
        }
        
        public void setTeamName(String teamName) {
            this.teamName = teamName;
        }
        
        public String getPostName() {
            return postName;
        }
        
        public void setPostName(String postName) {
            this.postName = postName;
        }
        
        public String getTeamMembers() {
            return teamMembers;
        }
        
        public void setTeamMembers(String teamMembers) {
            this.teamMembers = teamMembers;
        }
        
        public String getTeamEmail() {
            return teamEmail;
        }
        
        public void setTeamEmail(String teamEmail) {
            this.teamEmail = teamEmail;
        }
    }
} 