package com.javacodeex.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }

    public void sendHtmlEmail(String to, String subject, String message) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
    
        Context context = new Context();
        context.setVariable("subject", subject);
        context.setVariable("message", message);
        String htmlBody = templateEngine.process("emailTemplate", context);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
    
        mailSender.send(mimeMessage);
    }

    // Send bulk HTML emails to multiple recipients
    public List<String> sendBulkHtmlEmails(List<String> recipients, String subject, String message) {
        List<String> failedEmails = new ArrayList<>();
        
        for (String recipient : recipients) {
            try {
                sendHtmlEmail(recipient, subject, message);
            } catch (MessagingException e) {
                failedEmails.add(recipient);
                // Log the error but continue with other emails
                System.err.println("Failed to send email to " + recipient + ": " + e.getMessage());
            }
        }
        
        return failedEmails;
    }

    // Send notification email with dynamic content
    public void sendNotificationEmail(String to, String notificationType, Map<String, Object> data) throws MessagingException {
        String subject = "VSB Notification: " + notificationType;
        String message = generateNotificationMessage(notificationType, data);
        
        sendHtmlEmail(to, subject, message);
    }

    // Send welcome email to new volunteers
    public void sendWelcomeEmail(WelcomeEmailRequest request) throws MessagingException {
        String subject = "Welcome to Volunteer Skill Bank!";
        String message = generateWelcomeMessage(request);
        
        sendHtmlEmail(request.getEmail(), subject, message);
    }

    // Send application status update email
    public void sendApplicationStatusEmail(String to, String opportunityTitle, String status, String feedback) throws MessagingException {
        String subject = "Application Status Update: " + status;
        String message = generateApplicationStatusMessage(opportunityTitle, status, feedback);
        
        sendHtmlEmail(to, subject, message);
    }

    // Send task reminder email
    public void sendTaskReminderEmail(String to, String taskTitle, String date, String time, String location, String description) throws MessagingException {
        String subject = "Task Reminder: " + taskTitle;
        String message = generateTaskReminderMessage(taskTitle, date, time, location, description);
        
        sendHtmlEmail(to, subject, message);
    }

    // Send team update email
    public void sendTeamUpdateEmail(String to, String teamName, String projectName, String meetingDate, String location, String updateMessage) throws MessagingException {
        String subject = "Team Update: " + teamName;
        String message = generateTeamUpdateMessage(teamName, projectName, meetingDate, location, updateMessage);
        
        sendHtmlEmail(to, subject, message);
    }

    // Send individual application email
    public void sendIndividualApplicationEmail(String to, IndividualApplicationRequest request) throws MessagingException {
        String subject = "New Individual Volunteer Application - " + request.getOrganizationName();
        
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
    
        Context context = new Context();
        context.setVariable("organizationName", request.getOrganizationName());
        context.setVariable("fullName", request.getFullName());
        context.setVariable("dateOfBirth", request.getDateOfBirth());
        context.setVariable("email", request.getEmail());
        context.setVariable("phone", request.getPhone());
        context.setVariable("address", request.getAddress());
        context.setVariable("skills", request.getSkills());
        context.setVariable("applicationDate", request.getApplicationDate());
        context.setVariable("organizationEmail", request.getOrganizationEmail());
        
        String htmlBody = templateEngine.process("individualTemplate", context);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
    
        mailSender.send(mimeMessage);
    }

    // Send team application email
    public void sendTeamApplicationEmail(String to, TeamApplicationRequest request) throws MessagingException {
        String subject = "New Team Volunteer Application - " + request.getOrganizationName();
        
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
    
        Context context = new Context();
        context.setVariable("organizationName", request.getOrganizationName());
        context.setVariable("teamName", request.getTeamName());
        context.setVariable("leaderEmail", request.getLeaderEmail());
        context.setVariable("members", request.getMembers());
        context.setVariable("applicationDate", request.getApplicationDate());
        context.setVariable("organizationEmail", request.getOrganizationEmail());
        
        String htmlBody = templateEngine.process("teamTemplate", context);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
    
        mailSender.send(mimeMessage);
    }

    // Helper methods to generate email content
    private String generateNotificationMessage(String notificationType, Map<String, Object> data) {
        switch (notificationType) {
            case "new_opportunity":
                return "A new volunteering opportunity \"" + data.get("title") + "\" has been posted. Check it out!";
            case "application_received":
                return "Your application for \"" + data.get("opportunityTitle") + "\" has been received and is under review.";
            case "application_approved":
                return "Congratulations! Your application for \"" + data.get("opportunityTitle") + "\" has been approved.";
            case "application_rejected":
                return "Your application for \"" + data.get("opportunityTitle") + "\" was not selected at this time.";
            default:
                return "You have a new notification from Volunteer Skill Bank.";
        }
    }

    private String generateWelcomeMessage(WelcomeEmailRequest request) {
        StringBuilder skillsList = new StringBuilder();
        if (request.getSkills() != null && !request.getSkills().isEmpty()) {
            skillsList.append(String.join(", ", request.getSkills()));
        } else {
            skillsList.append("Not specified");
        }

        return String.format("""
            Welcome to Volunteer Skill Bank, %s!
            
            We're excited to have you join our community of volunteers making a difference.
            
            Your account has been successfully created with the following details:
            - Name: %s %s
            - Email: %s
            - Skills: %s
            
            You can now:
            - Browse volunteering opportunities
            - Apply for positions that match your skills
            - Track your volunteering hours
            - Connect with other volunteers
            
            If you have any questions, feel free to reach out to our support team.
            
            Best regards,
            The VSB Team
            """, 
            request.getFirstName(),
            request.getFirstName(), request.getLastName(),
            request.getEmail(),
            skillsList.toString()
        );
    }

    private String generateApplicationStatusMessage(String opportunityTitle, String status, String feedback) {
        String statusMessage;
        switch (status.toLowerCase()) {
            case "pending":
                statusMessage = "Your application is currently under review.";
                break;
            case "approved":
                statusMessage = "Congratulations! Your application has been approved.";
                break;
            case "rejected":
                statusMessage = "Thank you for your interest. Your application was not selected at this time.";
                break;
            case "withdrawn":
                statusMessage = "Your application has been withdrawn as requested.";
                break;
            default:
                statusMessage = "";
        }

        StringBuilder message = new StringBuilder();
        message.append("Application Status Update\n\n");
        message.append("Opportunity: ").append(opportunityTitle).append("\n");
        message.append("Status: ").append(status).append("\n");
        message.append(statusMessage).append("\n\n");

        if (feedback != null && !feedback.trim().isEmpty()) {
            message.append("Feedback: ").append(feedback).append("\n\n");
        }

        message.append("Thank you for your interest in volunteering with us.\n\n");
        message.append("Best regards,\nThe VSB Team");

        return message.toString();
    }

    private String generateTaskReminderMessage(String taskTitle, String date, String time, String location, String description) {
        return String.format("""
            Task Reminder
            
            You have a scheduled task coming up:
            
            Task: %s
            Date: %s
            Time: %s
            Location: %s
            Description: %s
            
            Please make sure to arrive on time and bring any necessary equipment.
            
            If you need to reschedule or have any questions, please contact the team leader.
            
            Best regards,
            The VSB Team
            """, 
            taskTitle, date, time, location, description
        );
    }

    private String generateTeamUpdateMessage(String teamName, String projectName, String meetingDate, String location, String updateMessage) {
        return String.format("""
            Team Update: %s
            
            %s
            
            Team Details:
            - Project: %s
            - Meeting Date: %s
            - Location: %s
            
            Please review the update and let us know if you have any questions.
            
            Best regards,
            The VSB Team
            """, 
            teamName, updateMessage, projectName, meetingDate, location
        );
    }

    // DTO for welcome email request
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

    // DTO for individual application request
    public static class IndividualApplicationRequest {
        private String fullName;
        private String dateOfBirth;
        private String email;
        private String phone;
        private String address;
        private String skills;
        private String organizationEmail;
        private String organizationName;
        private String applicationDate;

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
        public String getOrganizationName() { return organizationName; }
        public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }
        public String getApplicationDate() { return applicationDate; }
        public void setApplicationDate(String applicationDate) { this.applicationDate = applicationDate; }
    }

    // DTO for team application request
    public static class TeamApplicationRequest {
        private String organizationName;
        private String teamName;
        private String leaderEmail;
        private String members;
        private String organizationEmail;
        private String applicationDate;

        // Getters and Setters
        public String getOrganizationName() { return organizationName; }
        public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }
        public String getTeamName() { return teamName; }
        public void setTeamName(String teamName) { this.teamName = teamName; }
        public String getLeaderEmail() { return leaderEmail; }
        public void setLeaderEmail(String leaderEmail) { this.leaderEmail = leaderEmail; }
        public String getMembers() { return members; }
        public void setMembers(String members) { this.members = members; }
        public String getOrganizationEmail() { return organizationEmail; }
        public void setOrganizationEmail(String organizationEmail) { this.organizationEmail = organizationEmail; }
        public String getApplicationDate() { return applicationDate; }
        public void setApplicationDate(String applicationDate) { this.applicationDate = applicationDate; }
    }
}
