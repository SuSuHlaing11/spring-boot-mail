package com.javacodeex.controller;

import com.javacodeex.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@CrossOrigin(origins = "*")
public class EmailController {

    @Autowired
    private EmailService emailService;

    // GET endpoint for simple email sending (existing)
    @GetMapping("/send-email")
    public ResponseEntity<String> sendEmail(
        @RequestParam String to,
        @RequestParam String subject,
        @RequestParam String text
    ) {
        try {
            log.info("Sending email to: {}, subject: {}", to, subject);
            emailService.sendHtmlEmail(to, subject, text);
            return ResponseEntity.ok("Email sent successfully");
        } catch (MessagingException e) {
            log.error("Error sending email: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Failed to send email: " + e.getMessage());
        }
    }

    // POST endpoint for email sending with JSON body
    @PostMapping("/send-email")
    public ResponseEntity<Map<String, Object>> sendEmailPost(@RequestBody EmailRequest request) {
        try {
            log.info("Sending email to: {}, subject: {}", request.getTo(), request.getSubject());
            emailService.sendHtmlEmail(request.getTo(), request.getSubject(), request.getMessage());
            
            Map<String, Object> response = Map.of(
                "success", true,
                "message", "Email sent successfully",
                "timestamp", java.time.LocalDateTime.now().toString()
            );
            return ResponseEntity.ok(response);
        } catch (MessagingException e) {
            log.error("Error sending email: {}", e.getMessage());
            Map<String, Object> response = Map.of(
                "success", false,
                "message", "Failed to send email: " + e.getMessage(),
                "timestamp", java.time.LocalDateTime.now().toString()
            );
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // POST endpoint for bulk email sending
    @PostMapping("/send-bulk-email")
    public ResponseEntity<Map<String, Object>> sendBulkEmail(@RequestBody BulkEmailRequest request) {
        try {
            log.info("Sending bulk email to {} recipients, subject: {}", 
                request.getRecipients().size(), request.getSubject());
            
            List<String> failedEmails = emailService.sendBulkHtmlEmails(
                request.getRecipients(), 
                request.getSubject(), 
                request.getMessage()
            );
            
            Map<String, Object> response = Map.of(
                "success", true,
                "message", "Bulk email sent successfully",
                "totalRecipients", request.getRecipients().size(),
                "failedEmails", failedEmails,
                "timestamp", java.time.LocalDateTime.now().toString()
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error sending bulk email: {}", e.getMessage());
            Map<String, Object> response = Map.of(
                "success", false,
                "message", "Failed to send bulk email: " + e.getMessage(),
                "timestamp", java.time.LocalDateTime.now().toString()
            );
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // POST endpoint for notification emails
    @PostMapping("/send-notification")
    public ResponseEntity<Map<String, Object>> sendNotificationEmail(@RequestBody NotificationEmailRequest request) {
        try {
            log.info("Sending notification email to: {}, type: {}", request.getTo(), request.getType());
            emailService.sendNotificationEmail(request.getTo(), request.getType(), request.getData());
            
            Map<String, Object> response = Map.of(
                "success", true,
                "message", "Notification email sent successfully",
                "timestamp", java.time.LocalDateTime.now().toString()
            );
            return ResponseEntity.ok(response);
        } catch (MessagingException e) {
            log.error("Error sending notification email: {}", e.getMessage());
            Map<String, Object> response = Map.of(
                "success", false,
                "message", "Failed to send notification email: " + e.getMessage(),
                "timestamp", java.time.LocalDateTime.now().toString()
            );
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // POST endpoint for welcome emails
    @PostMapping("/send-welcome")
    public ResponseEntity<Map<String, Object>> sendWelcomeEmail(@RequestBody WelcomeEmailRequest request) {
        try {
            log.info("Sending welcome email to: {}", request.getEmail());
            EmailService.WelcomeEmailRequest serviceRequest = new EmailService.WelcomeEmailRequest();
            serviceRequest.setEmail(request.getEmail());
            serviceRequest.setFirstName(request.getFirstName());
            serviceRequest.setLastName(request.getLastName());
            serviceRequest.setSkills(request.getSkills());
            emailService.sendWelcomeEmail(serviceRequest);
            
            Map<String, Object> response = Map.of(
                "success", true,
                "message", "Welcome email sent successfully",
                "timestamp", java.time.LocalDateTime.now().toString()
            );
            return ResponseEntity.ok(response);
        } catch (MessagingException e) {
            log.error("Error sending welcome email: {}", e.getMessage());
            Map<String, Object> response = Map.of(
                "success", false,
                "message", "Failed to send welcome email: " + e.getMessage(),
                "timestamp", java.time.LocalDateTime.now().toString()
            );
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // POST endpoint for individual application emails
    @PostMapping("/send-individual-application")
    public ResponseEntity<Map<String, Object>> sendIndividualApplicationEmail(@RequestBody IndividualApplicationEmailRequest request) {
        try {
            log.info("Sending individual application email to: {}", request.getOrganizationEmail());
            EmailService.IndividualApplicationRequest serviceRequest = new EmailService.IndividualApplicationRequest();
            serviceRequest.setDateOfBirth(request.getDateOfBirth());
            serviceRequest.setEmail(request.getEmail());
            serviceRequest.setPhone(request.getPhone());
            serviceRequest.setAddress(request.getAddress());
            serviceRequest.setSkills(request.getSkills());
            
            emailService.sendIndividualApplicationEmail(request.getOrganizationEmail(), serviceRequest);
            
            Map<String, Object> response = Map.of(
                "success", true,
                "message", "Individual application email sent successfully",
                "timestamp", java.time.LocalDateTime.now().toString()
            );
            return ResponseEntity.ok(response);
        } catch (MessagingException e) {
            log.error("Error sending individual application email: {}", e.getMessage());
            Map<String, Object> response = Map.of(
                "success", false,
                "message", "Failed to send individual application email: " + e.getMessage(),
                "timestamp", java.time.LocalDateTime.now().toString()
            );
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // POST endpoint for team application emails
    @PostMapping("/send-team-application")
    public ResponseEntity<Map<String, Object>> sendTeamApplicationEmail(@RequestBody TeamApplicationEmailRequest request) {
        try {
            log.info("Sending team application email to: {}", request.getOrganizationEmail());
            EmailService.TeamApplicationRequest serviceRequest = new EmailService.TeamApplicationRequest();
            serviceRequest.setTeamName(request.getTeamName());
            serviceRequest.setLeaderEmail(request.getLeaderEmail());
            serviceRequest.setMembers(request.getMembers());
            serviceRequest.setOrganizationEmail(request.getOrganizationEmail());
            
            emailService.sendTeamApplicationEmail(request.getOrganizationEmail(), serviceRequest);
            
            Map<String, Object> response = Map.of(
                "success", true,
                "message", "Team application email sent successfully",
                "timestamp", java.time.LocalDateTime.now().toString()
            );
            return ResponseEntity.ok(response);
        } catch (MessagingException e) {
            log.error("Error sending team application email: {}", e.getMessage());
            Map<String, Object> response = Map.of(
                "success", false,
                "message", "Failed to send team application email: " + e.getMessage(),
                "timestamp", java.time.LocalDateTime.now().toString()
            );
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // Health check endpoint
    @GetMapping("/email/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = Map.of(
            "status", "UP",
            "service", "Email Service",
            "timestamp", java.time.LocalDateTime.now().toString()
        );
        return ResponseEntity.ok(response);
    }

    // Request DTOs
    public static class EmailRequest {
        private String to;
        private String subject;
        private String message;

        // Getters and Setters
        public String getTo() { return to; }
        public void setTo(String to) { this.to = to; }
        public String getSubject() { return subject; }
        public void setSubject(String subject) { this.subject = subject; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    public static class BulkEmailRequest {
        private List<String> recipients;
        private String subject;
        private String message;

        // Getters and Setters
        public List<String> getRecipients() { return recipients; }
        public void setRecipients(List<String> recipients) { this.recipients = recipients; }
        public String getSubject() { return subject; }
        public void setSubject(String subject) { this.subject = subject; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    public static class NotificationEmailRequest {
        private String to;
        private String type;
        private Map<String, Object> data;

        // Getters and Setters
        public String getTo() { return to; }
        public void setTo(String to) { this.to = to; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public Map<String, Object> getData() { return data; }
        public void setData(Map<String, Object> data) { this.data = data; }
    }

    public static class WelcomeEmailRequest {
        private String email;
        private String firstName;
        private String lastName;
        private List<String> skills;

        // Getters and Setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        public List<String> getSkills() { return skills; }
        public void setSkills(List<String> skills) { this.skills = skills; }
    }

    public static class IndividualApplicationEmailRequest {
        private String fullName;
        private String dateOfBirth;
        private String email;
        private String phone;
        private String address;
        private String skills;
        private String organizationEmail;

        // Getters and Setters
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public String getDateOfBirth() { return dateOfBirth; }
        public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
        public String getSkills() { return skills; }
        public void setSkills(String skills) { this.skills = skills; }
        public String getOrganizationEmail() { return organizationEmail; }
        public void setOrganizationEmail(String organizationEmail) { this.organizationEmail = organizationEmail; }
    }

    public static class TeamApplicationEmailRequest {
        private String organizationName;
        private String teamName;
        private String leaderEmail;
        private String members;
        private String organizationEmail;
        private String applicationDate;

        // Getters and Setters
        public String getTeamName() { return teamName; }
        public void setTeamName(String teamName) { this.teamName = teamName; }
        public String getLeaderEmail() { return leaderEmail; }
        public void setLeaderEmail(String leaderEmail) { this.leaderEmail = leaderEmail; }
        public String getMembers() { return members; }
        public void setMembers(String members) { this.members = members; }
        public String getOrganizationEmail() { return organizationEmail; }
        public void setOrganizationEmail(String organizationEmail) { this.organizationEmail = organizationEmail; }
    }
}

