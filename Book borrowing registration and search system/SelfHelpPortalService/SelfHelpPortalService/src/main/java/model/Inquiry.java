package model;

import java.time.LocalDateTime;

public class Inquiry {
    private LocalDateTime createdAt; 
    private String inquirerEmail; 
    private String subject; 
    private String content;
    private String assignedTo; 

    public Inquiry(String inquirySubject, String inquiryContent, String emailAddress) {
        this.createdAt = LocalDateTime.now();
        this.inquirerEmail = emailAddress; 
        this.subject = inquirySubject;
        this.content = inquiryContent;
        this.assignedTo = "Admin";
    }


    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public String getInquirerEmail() {
        return this.inquirerEmail;
    }

    public String getSubject() {
        return this.subject;
    }

    public String getContent() {
        return this.content;
    }
    
    public String getAssignedTo(){return this.assignedTo;}

    public void setAssignedTo(String assignmentEmail){this.assignedTo = assignmentEmail;}
}