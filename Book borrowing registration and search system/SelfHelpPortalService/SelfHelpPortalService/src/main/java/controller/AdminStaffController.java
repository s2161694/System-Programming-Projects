package controller;

import external.AuthenticationService;
import external.EmailService;
import model.FAQ;
import model.FAQItem;
import model.SharedContext;
import view.View;
import model.FAQSection;
import model.Inquiry;
import model.Page;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.Iterator;

public class AdminStaffController extends StaffController {

    public AdminStaffController(SharedContext context, View view, AuthenticationService authenticationService, EmailService emailService) {
        super(context, view, authenticationService, emailService);
    }

    public void addPage()  {
        String title = controllerView.getInput("Enter page title");
        String content  = controllerView.getInput("Enter page content");
        boolean isPrivate = controllerView.getYesNoInput("Should this page be private?");
        Collection<Page> availablePages = controllerContext.getPages();
        boolean titleExists = false;
        for (Page page: availablePages) 
        {
            if (page.getTitle() == title) 
            {
                titleExists = true;
            }
        }

        if (titleExists == true) 
        {
            boolean overwrite = controllerView.getYesNoInput("Page: " + title + "already exists. Overwrite with the page?");
            if (overwrite == false) 
            {
                controllerView.displayInfo("Cancelled adding new page");
                return;
            }
        }

        // adding the page
        Page newPage = new Page(title, content, isPrivate);
        controllerContext.addPage(newPage);

        // The "notify" bit
        int StatusCode = controllerEmail.sendEmail(controllerContext.getUserEmailContext(), controllerContext.adminStaffEmail, title, content);
        if (StatusCode == controllerEmail.STATUS_SUCCESS) {
            controllerView.displaySuccess("Added page " + title + "");
        }
        else
        {
            controllerView.displayWarning("Added page " + title + " but failed to send email notification!");
        }
        return;

    }

    public void manageFAQ()
    {
        String faqInput = "";
        FAQ faq = controllerContext.getfaQList();
        int faqInput_int;   
        int temp_count = 0;
        int trace_index = -1;
        Collection<FAQSection> faqList = faq.getfaqSection().getSubsections();
        FAQSection current_FAQSection = new FAQSection("");
        int previous_size_list, previous_faqInput_int;
        ArrayList<FAQSection> trace = new ArrayList<>();
        if(faqList.isEmpty())
        {
            controllerView.displayError("Sorry! No items to display.");
        }
        else
        {
            while (!faqInput.matches("e")) 
            {
                if (faqInput.isEmpty())
                {
                    controllerView.displayFAQ(faq, true);
                    boolean toAddItem = controllerView.getYesNoInput("Do you want to add a new question-answer pair here? ");
                    if (toAddItem == true) {
                        addFAQItem(faq.getfaqSection());
                    }
                    faqInput = controllerView.getInput(
                            "Please select an option by entering its " +
                                    "corresponding number(or to exit please type e): ");
                    if(faqInput.matches("0+"))
                    {
                        faqInput = "";
                    }
                }
                if (!faqInput.matches("\\d+") && !(faqInput.matches("e")))
                {
                    controllerView.displayError("Please enter a valid input");
                    faqInput = "";
                }
                else if (faqInput.matches("\\d+"))
                {
                    faqInput_int = Integer.parseInt(faqInput);
                    faqInput_int--;
                    int size_list = 0;
                    for (FAQSection e : faqList) {
                        size_list++;
                        if (temp_count++ == faqInput_int) {
                            current_FAQSection = e;
                            break;
                        }
                    }
                    if (faqInput_int <= size_list)
                    {
                        trace.add(current_FAQSection);
                        trace_index++;
                        previous_faqInput_int = faqInput_int;
                        previous_size_list = size_list;
                        while(!trace.isEmpty())
                        {
                            if (faqInput_int <= size_list)
                            {
                                previous_faqInput_int = faqInput_int;
                                previous_size_list = size_list;
                                controllerView.displayFAQSection(current_FAQSection, true);
                                boolean toAddItem = controllerView.getYesNoInput("Do you want to add a new question-answer pair here? ");
                                if (toAddItem == true) 
                                {
                                    addFAQItem(current_FAQSection);
                                }
                                faqInput = controllerView.getInput(
                                        "Please select a subsection by entering its " +
                                                "corresponding number(to exit please type e, " +
                                                "to go up one level please type b: ");
                                if (faqInput.matches("b"))
                                {
                                    trace.remove(trace_index--);
                                    faqInput = "";
                                    current_FAQSection = current_FAQSection.getparent();
                                }
                                else if (faqInput.matches("\\d+")) {
                                    size_list = 0;
                                    temp_count = 0;
                                    faqInput_int = Integer.parseInt(faqInput);
                                    faqInput_int--;
                                    for (FAQSection e : trace.get(trace_index).getSubsections()) {
                                        size_list++;
                                        if (temp_count++ == faqInput_int) {
                                            trace.add(e);
                                            current_FAQSection = e;
                                            trace_index++;
                                            break;
                                        }
                                    }
                                }
                                else if(faqInput.matches("e"))
                                {
                                    trace.clear();
                                }
                            }
                            else
                            {
                                controllerView.displayError("Please enter a valid input");
                                faqInput_int = previous_faqInput_int;
                                size_list = previous_size_list;
                            }
                        }
                    }
                    else {
                        controllerView.displayError("Please enter a valid input");
                        faqInput = "";
                    }

                }
                else if(faqInput.matches("e"))
                    break;
            }
        }

    }

    private void addFAQItem(FAQSection faqSection) {
        // We are assuming that the faqSection here is the parent section for the faqItem (QA pair) that is going to be added.
        // We are also assuming that this function is going to be used as the input parsed from the managingFAQ function indicates
        // that the user is wanting to add a QA pair.


        // get the FAQItem information from the user
        controllerView.displayDivider();
        String question = controllerView.getInput("Write the question: ");
        String answer = controllerView.getInput("Write the answer: ");

        // check if the faqSection is root.
        if (faqSection == controllerContext.getfaQList().getfaqSection()) {
            // create a mandotary section
            controllerView.displayDivider();
            String title = controllerView.getInput("Write the title of the section topic: ");
            // fetch the subSections of this section
            Collection<FAQSection> subSections = faqSection.getSubsections();

            // check if the title already exists
            for (FAQSection subSection: subSections)
            {
                // if it exists then insert the QA pair to this subSection
                if ((subSection.getTopic()).equals(title))
                {
                    controllerView.displayWarning("Section topic already exists. The new question-answer pair will be added to it");
                    subSection.addItem(question, answer);
                }
                else 
                {
                    // make a new subsection with the new title and add the QA pair to it.
                    FAQSection newSection = new FAQSection(title);
                    newSection.addItem(question, answer);
                    // insert to the new subsection to the first level of the FAQ where it belongs
                    controllerContext.getfaQList().addSection(newSection);
                }
            }    
        }
        else
        {
            boolean sectionRequired = controllerView.getYesNoInput("Do you want to create a new section for your Question-Answer item? ");
            if (sectionRequired == true) 
            {
                // create the new section
                String title = controllerView.getInput("Write the title of this new section: ");
                // fetch the subSections of this section 

                // check if the title already exists
                Collection<FAQSection> subSections = faqSection.getSubsections();
                for (FAQSection subSection: subSections)
                {
                    // if it exists then insert the QA pair to this subSection
                    if ((subSection.getTopic()).equals(title))
                    {
                        controllerView.displayWarning("Section topic already exists. The new question-answer pair will be added to it");
                        controllerView.displayDivider();
                        subSection.addItem(question, answer);
                    }
                    else
                    {
                        FAQSection newSection = new FAQSection(title);
                        newSection.addItem(question, answer);
                        // Add it to the parent section
                        faqSection.addSubsection(newSection);
                    }
                }
            }
            else
            {
                faqSection.addItem(question, answer);
            }
        }

        //////////// send updates and notifications //////////////////////

        // Subject + Sender done
        String emailSubject = "The topic named " + faqSection.getTopic() + " you requested updates on has been updated";
        String sender = controllerContext.getUserEmailContext();

        // Content done
        // fetch email content "that is the list of all the QAs in its first level plus the newly-added QA"
        Collection<FAQItem> faqItems = new ArrayList<>();
        faqItems = faqSection.getFaqItems();
        faqItems.add(new FAQItem(question, answer));
        StringBuilder sb = new StringBuilder();
        for (FAQItem item : faqItems) {
            sb.append("Question: ").append(item.getQuestion()).append("\n");
            sb.append("Answer: ").append(item.getAnswer()).append("\n\n");
        }
        String emailContent = sb.toString();

        // check if the topic exists in the faqTopicsUpdateSubscribers
        boolean topicExists = false;
        String topicChanged = faqSection.getTopic();
        HashMap<String, Collection <String>> faqTopicsUpdateSubscribers = controllerContext.getFaqTopicsUpdateSubscribers();
        for (Map.Entry<String, Collection<String>> entry: faqTopicsUpdateSubscribers.entrySet())
        {
            String topicForNotifications = entry.getKey();
            if (topicForNotifications.equals(topicChanged))
            {
                topicExists = true;                
            }
        }

        // if the topic exists then: 
        // we modify the recipient list to include the subscibers to the topic.
        
        // recipient list done
        Collection<String> recipients = new ArrayList<>();
        if (topicExists == true)
        {
            // fetch the list of recipeients
            for (Map.Entry<String, Collection<String>> entry: faqTopicsUpdateSubscribers.entrySet())
            {
                String topicForNotifications = entry.getKey();
                if (topicChanged.equals(topicForNotifications))
                {
                    recipients = entry.getValue();
                    recipients.add(controllerContext.adminStaffEmail);
                }
            }
        }
        else
        {
            recipients.add(controllerContext.adminStaffEmail);
        }

        // we will send emails to the collection of recipeints:
        // the collection is a implemented as an array list so we can use a for loop.

        for (String recipient: recipients)
        {
            int statusCode = controllerEmail.sendEmail(sender, recipient, emailSubject, emailContent);
            if (statusCode == controllerEmail.STATUS_SUCCESS)
            {
                controllerView.displayDivider();
                controllerView.displaySuccess("Admin Staff and any existing topic subscribers have been successfully notified of any changes made!");
            } else
            {
                controllerView.displayDivider();
                controllerView.displayWarning("An error has happened while trying to update admin staff and topic subscriberrs of any changes made.");
            }
        }
        
    }


    public void viewAllPages()
    {
        // fetching webpages' titles form the data storage in the model.
        Collection<Page> pages = controllerContext.getPages();
        Collection<String> pagesTitles = new ArrayList<>();
        for (Page page: pages) {
            pagesTitles.add(page.getTitle());
        }

        // Displaying a numbered list of webpage titles on the console
        // Definition: listItemNumbers is the number the corresponds to each item in a list ( 1. item  2. another item 3. a third item )
        int listItemNumbers = 0;     
        for(String pageTitle: pagesTitles) 
        {
            listItemNumbers+= 1;
            controllerView.displayInfo(String.format("%d." + pageTitle + "\n", listItemNumbers));
        }

        // getting user input as the user chooses an option from the menu
        String result = controllerView.getInput(String.format("Choose a number from 1 to %d to select an option or enter e to exit: ", listItemNumbers));

        if (result.equals("e")) 
        {

        }
        else if (result.matches("\\d+"))
        {
            int result_int = Integer.parseInt(result);
            if ((result_int <= 0) || (result_int > listItemNumbers))
            {
                controllerView.displayWarning("Please enter a valid number\n");
                viewAllPages();
            }
            else
            {
                listItemNumbers = 0;
                for (Page page: pages)
                {
                    if (listItemNumbers++ == result_int)
                    {
                        controllerView.displayInfo(page.getContent());
                        boolean viewAllWebPages = controllerView.getYesNoInput("Do you want to go back to view all webpages again? ");
                        if (viewAllWebPages == true)
                        {
                            viewAllPages();
                        }
                    }
                }
        
            }
        }
        else 
        {
            controllerView.displayWarning("Please enter a valid number\n");
            viewAllPages();
        }
 
    }

    public void manageInquiries()
    {
        Collection<Inquiry> queries = controllerContext.getUserInquiry();
        Collection<Inquiry> adminQueries = new ArrayList<>();
        Iterator<Inquiry> queriesIter = queries.iterator();

        while (queriesIter.hasNext()) {
            Inquiry query = queriesIter.next();
            if(query.getAssignedTo().equals(controllerContext.getAdminStaffEmail()) ||
                    (query.getAssignedTo().equals("Admin")))
                adminQueries.add(query);
        }

        Collection<String> inquiryTitles = getInquiryTitles(adminQueries);
        int temp_count = 0;

        // displaying all the queries on the screen.
        Iterator<String> inquiryTitlesIter = inquiryTitles.iterator();
        while(inquiryTitlesIter.hasNext())
        {
            temp_count+= 1;
            String title = inquiryTitlesIter.next();
            controllerView.displayInfo(String.format("%d.", temp_count));
            controllerView.displayInfo(title + "\n");
        }

        String result = controllerView.getInput(String.format("Please choose a number(from 1 to %d) " +
                "to select the option(to exit enter e): ", temp_count));
        controllerView.displayDivider();
        if(result.equals("e"))
        {}
        else if(result.matches("\\d+"))
        {
            int result_int;
            result_int = Integer.parseInt(result);
            if ((result_int <= 0) || (result_int > temp_count)) {
                controllerView.displayWarning("Please enter a valid number\n");
                manageInquiries();
            }
            else
            {
                temp_count = 0;
                for(Inquiry query: adminQueries)
                {
                    if(temp_count++ == result_int)
                    {
                        // prompt them to either respond or redirect
                        result = controllerView.getInput("To respond input r, to redirect the query input d: ");
                        if (result.equals("r"))
                        {
                            respondToInquiry(query);
                        }
                        else if (result.equals("d"))
                        {
                            redirectInquiry(query);
                        }
                    }
                }
                manageInquiries();
            }
        }
        else {
            controllerView.displayWarning("Please enter a valid number\n");
            manageInquiries();
        }
    }

    private void redirectInquiry(Inquiry inquiry) {
        controllerView.displayDivider();
        String staffEmail = controllerView.getInput("Write the email of the staff member you want to redirect this query to: ");
        int statusCode = controllerEmail.sendEmail(staffEmail, inquiry.getInquirerEmail(), "New Inquiry",
        "You have been assigned a new query. Please login to the kiosk machine to view it. " +
                "Subject of the Inquiry: " + inquiry.getSubject());
        
        if (statusCode == controllerEmail.STATUS_SUCCESS) 
        {
            inquiry.setAssignedTo(staffEmail);
            controllerView.displaySuccess("Inquiry has been redirected successfully"); 
            controllerView.displayDivider();
            return;
        }
        if (statusCode == controllerEmail.STATUS_INVALID_RECIPIENT_EMAIL)
        {
            controllerView.displayWarning("Recipient Email is invalid. Rewrite the email of the staff member you want to redirect this query to: ");
            redirectInquiry(inquiry);
        }
        if (statusCode == controllerEmail.STATUS_UNKNOWN_ERROR)
        {
            controllerView.displayError("There has been an unknown error. We could not send the inquiry to the recipeint. Try again later!");
            controllerView.displayDivider();
            return;
        }
    }



}


            
            