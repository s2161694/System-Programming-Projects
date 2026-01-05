package model;

import java.util.Collection;
import java.util.HashMap; 
import java.util.HashSet;
import java.util.ArrayList;
import external.MockEmailService; 
import external.EmailService;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.Iterator;
import java.util.Collection;


public class SharedContext{
    public String adminStaffEmail; 
    private HashMap<String, Collection <String>> faqTopicsUpdateSubscribers = new HashMap<String, Collection<String>>(); 
    private User currentUser;
    private FAQ fAQList;
    private Collection<Inquiry> userInquiry;
    private EmailService emailService;
    private Collection<Page> pages;

    public SharedContext() {
        this.fAQList = new FAQ();
        this.userInquiry = new ArrayList<Inquiry>();
        this.emailService = new MockEmailService();
        this.currentUser = new Guest();
        this.pages = new ArrayList<>();
    }

    public void setFAQSection(FAQSection section) {
        // Check if the section and its topic are valid
        if (section != null && section.getTopic() != null && !section.getTopic().isEmpty()) {
            Iterator<FAQSection> iterator = this.fAQList.getfaqSection().getSubsections().iterator();
            boolean sectionFound = false;

            while (iterator.hasNext()) {
                FAQSection existingSection = iterator.next();
                if (isSameTopic(section, existingSection)) {
                    // Found a matching section, modify it
                    Collection<FAQItem> newItems = section.getFaqItems();
                    if (!newItems.isEmpty()) {
                        existingSection.getFaqItems().addAll(newItems);
                        System.out.println("Added new FAQItem(s) to existing FAQ section.");
                    } else {
                        System.out.println("WARNING: New FAQ section is empty. No FAQItem added.");
                    }
                    sectionFound = true;
                    break;
                }
            }

            if (!sectionFound) {
                // If no matching section found, add the new section to the FAQList
                this.fAQList.addSection(section);
            }
        } else {
            // Handle the case where input is invalid (null or empty topic)
            if (section == null) {
                System.out.println("WARNING: Cannot add a null FAQ section.");
            } else if (section.getTopic() == null || section.getTopic().isEmpty()) {
                System.out.println("WARNING: FAQ section topic cannot be null or empty.");
            }

        }
    }
    private boolean isSameTopic(FAQSection section1, FAQSection section2) {
        // Compare titles and hierarchy positions
        return section1.getTopic().equals(section2.getTopic());
    }
    public FAQItem getItem(Collection<FAQItem> items) {
        return items.stream().findFirst().orElse(null);
    }
    public void addPage(Page page){
        this.pages.add(page);
    }
    public String getUserRoleContext() {
        if(currentUser.getClass().getSimpleName().equals("AuthenticatedUser"))
            return currentUser.getUserRole();
        else
            return "Guest";
    }
    public FAQ getfaQList()
    {
        return this.fAQList;
    }
    public void setCurrentUser(String role, String email) {
        if(role.isEmpty() && email.isEmpty())
            currentUser = new Guest();
        else
        {
            currentUser = new AuthenticatedUser(role, email);
        }
    }

    public String getUserEmailContext(){return currentUser.getEmail();}
    public String getAdminStaffEmail() {
        return adminStaffEmail;
    }
    public HashMap<String, Collection <String>> getFaqTopicsUpdateSubscribers(){
        return this.faqTopicsUpdateSubscribers;
    }
    public void setFaqTopicsUpdateSubscribers(HashMap<String, Collection <String>> subs) {
        this.faqTopicsUpdateSubscribers = subs;
    }
    public HashMap<String, Collection <String>>  setSubs() {
        return this.faqTopicsUpdateSubscribers;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public Collection<Inquiry> getUserInquiry(){return this.userInquiry;}

    public void setUserInquiry(Collection<Inquiry> newQueries){userInquiry = newQueries;}

    public void setQuery(Inquiry query){userInquiry.add(query);}

    public Collection<Page> getPages(){return this.pages;}

    public boolean registerForFAQUpdates(String Email, String topic) {
       Collection<String> topicfaqTopicsUpdateSubscribers = faqTopicsUpdateSubscribers.get(topic);
        if (topicfaqTopicsUpdateSubscribers.contains(Email)) {
            return false;
        }
        topicfaqTopicsUpdateSubscribers.add(Email);
        return true;
    }
    public boolean unregisterForFAQUpdates(String email, String topic) {
        return faqTopicsUpdateSubscribers.get(topic).remove(email);
    }
    public Collection<String> usersSubscribedToFAQTopic(String topic) {
        return faqTopicsUpdateSubscribers.getOrDefault(topic, new HashSet<>());
    }
}    