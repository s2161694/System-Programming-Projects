package controller;

import external.AuthenticationService;
import external.EmailService;
import model.*;
import org.apache.lucene.queryparser.classic.ParseException;
import view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;

public class InquirerController extends Controller {

    public InquirerController(SharedContext context, View view, AuthenticationService authenticationService, EmailService emailService) {
        this.controllerView = view;
        this.controllerContext = context;
        this.controllerAuth = authenticationService;
        this.controllerEmail = emailService;
    }

    @Override
    protected void login() {}

    @Override
    protected void logout() {}

    public void consultFAQ()
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
            while (!faqInput.matches("e")) {
                if (faqInput.isEmpty())
                {
                    controllerView.displayFAQ(faq, true);
                    faqInput = controllerView.getInput(
                            "Please select an option by entering its " +
                                    "corresponding number(or to exit please type e): ");
                    controllerView.displayDivider();
                    if(faqInput.matches("0+"))
                        faqInput = "";
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
                                faqInput = controllerView.getInput(
                                        "Please select a subsection by entering its " +
                                                "corresponding number(to exit please type e, " +
                                                "to go up one level please type b, to request updates " +
                                                "please type u, or to unregister please type o): ");
                                controllerView.displayDivider();
                                if (faqInput.matches("b"))
                                {
                                    trace.remove(trace_index--);
                                    faqInput = "";
                                    current_FAQSection = current_FAQSection.getparent();
                                }
                                else if (faqInput.matches("o"))
                                {
                                    stopFAQUpdates(current_FAQSection.getTopic(), "");
                                }
                                else if(faqInput.equals("u"))
                                {
                                    requestFAQUpdates(current_FAQSection.getTopic(), "");
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

    public void searchPages()
    {
        try {
            PageSearch pageSearchObj = new PageSearch(controllerContext.getPages());
            controllerView.displayInfo("Search pages\n");
            String searchQuery = controllerView.getInput("Please enter your search query: ");
            while(searchQuery.isEmpty())
            {
                controllerView.displayWarning("Please enter a valid input.\n");
                searchQuery = controllerView.getInput("Please enter your search query: ");
            }
            Collection<PageSearchResult> results = pageSearchObj.search(searchQuery);
            if(controllerContext.getUserRoleContext().equals("Guest"))
            {
                String temp_string;
                boolean elemPublic;
                Collection<PageSearchResult> searchResults = new ArrayList<>();
                for(PageSearchResult element: results)
                {
                    temp_string = element.getFormattedContent();
                    elemPublic = temp_string.endsWith("isPrivate: false");
                    if(elemPublic)
                        searchResults.add(new PageSearchResult(
                                temp_string.substring(0, temp_string.length() - 16)));
                }
                controllerView.displaySearchResults(searchResults);
            }
            else
            {
                String temp_string;
                boolean elemPublic;
                Collection<PageSearchResult> searchResults = new ArrayList<>();
                for(PageSearchResult element: results)
                {
                    temp_string = element.getFormattedContent();
                    elemPublic = temp_string.endsWith("isPrivate: false");
                    if(elemPublic)
                        searchResults.add(new PageSearchResult(
                                temp_string.substring(0, temp_string.length() - 16)));
                    else
                        searchResults.add(new PageSearchResult(
                                temp_string.substring(0, temp_string.length() - 15)));
                }
                controllerView.displaySearchResults(searchResults);
            }
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public String getEmail()
    {
        String eMail = controllerView.getInput("Please enter your email: ");
        Pattern p = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$");
        while(!p.matcher(eMail).matches())
        {
            controllerView.displayError("Please enter a valid input\n");
            eMail = controllerView.getInput("Please enter your email: ");
        }
        return eMail;
    }

    public void contactStaff()
    {
        controllerView.displayInfo("Consult Staff\n");
        String inquirySubject = controllerView.getInput("Subject of your query: ");
        String inquiryContent = controllerView.getInput("Content of your query: ");
        String email;
        if(controllerContext.getUserRoleContext().equals("Guest")) 
        {
            email = getEmail();
        }    
        else
        {
            email = controllerContext.getUserEmailContext();
        }

        Inquiry query = new Inquiry(inquirySubject, inquiryContent, email);

        query.setAssignedTo(controllerContext.getAdminStaffEmail());

        controllerContext.setQuery(query);
        
        int StatusCode = controllerEmail.sendEmail(email, controllerContext.getAdminStaffEmail(),
                inquirySubject, inquiryContent);
        if (StatusCode == controllerEmail.STATUS_SUCCESS)
        {
            controllerView.displaySuccess("Your query has been sent successfully!");
        }
        if (StatusCode == controllerEmail.STATUS_INVALID_SENDER_EMAIL)
        {
            controllerView.displayWarning("Sender email is invalid. Please provide a valid email");
            contactStaff();
        }
        if (StatusCode == controllerEmail.STATUS_INVALID_RECIPIENT_EMAIL)
        {
            controllerView.displayError("There has been a problem with sending the email. " +
                    "Please try again later.");
        }
    }

    private void requestFAQUpdates(String topic, String email)
    {
        if(email.isEmpty())
            email = getEmail();
        boolean confirmation = controllerContext.registerForFAQUpdates(email, topic);
        if(confirmation)
        {
            controllerView.displaySuccess("Registered email for updates\n");
        }
        else
        {
            controllerView.displayWarning("Email already exists\n");
        }
    }
    private void stopFAQUpdates(String topic, String email)
    {
        if(email.isEmpty())
            email = getEmail();
        boolean confirmation = controllerContext.unregisterForFAQUpdates(email, topic);
        if(confirmation)
        {
            controllerView.displaySuccess("Removed email from updates\n");
        }
        else
        {
            controllerView.displayWarning("Email doesn't exist in the list\n");
        }
    }
}