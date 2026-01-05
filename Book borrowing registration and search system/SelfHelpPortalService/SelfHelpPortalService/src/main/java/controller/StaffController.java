package controller;

import external.AuthenticationService;
import external.EmailService;
import model.SharedContext;
import view.View;
import model.Inquiry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Pattern;


class StaffController extends Controller {
    public StaffController(SharedContext context, View view, AuthenticationService authenticationService, EmailService emailService){
        this.controllerView = view;
        this.controllerContext = context;
        this.controllerAuth = authenticationService;
        this.controllerEmail = emailService;
    }

    @Override
    protected void login() {}

    @Override
    protected void logout() {}

    protected Collection<String> getInquiryTitles(Collection<Inquiry> inquiries) {
        Iterator<Inquiry> iteratorOnQueries = inquiries.iterator();
        Collection<String> titles = new ArrayList<>();
        while (iteratorOnQueries.hasNext())
        {
            Inquiry query = iteratorOnQueries.next();
            titles.add(query.getSubject());
        }
        return titles;
    }

    @SuppressWarnings("static-access")
    protected void respondToInquiry(Inquiry inquiry) {
        String result;
        String inquirerEmail = inquiry.getInquirerEmail();
        String staffEmail;
        controllerView.displayDivider();
        controllerView.displayInquiry(inquiry);
        if (controllerContext.getUserRoleContext().equals("TeachingStaff")) {            
            staffEmail = controllerContext.getCurrentUser().getEmail();
        }
        else {
            staffEmail = controllerContext.adminStaffEmail;
        }
        controllerView.displayDivider();
        result = controllerView.getInput("To respond input r, to go back please input b: ");
        controllerView.displayDivider();
        
        if (result.equals("r")) {
            String responseSubject = controllerView.getInput("Enter the title of your response: ");
            String responseContent = controllerView.getInput("Enter the content of your response: ");
            int statusCode = controllerEmail.sendEmail(staffEmail, inquirerEmail, responseSubject, responseContent);
            controllerView.displayDivider();
            if (statusCode == controllerEmail.STATUS_SUCCESS) 
            {
                controllerView.displaySuccess("Your response to query has been sent successfully!");
            }
            while (statusCode == controllerEmail.STATUS_INVALID_SENDER_EMAIL)
            {
                controllerView.displayWarning("Your registered email is invalid. Please provide a valid email.\n");
                controllerView.displayDivider();
                staffEmail = controllerView.getInput("Enter your email: ");
                controllerContext.getCurrentUser().setEmail(staffEmail);
                statusCode = controllerEmail.sendEmail(staffEmail, inquirerEmail, responseSubject, responseContent);
            }
            if (statusCode == controllerEmail.STATUS_INVALID_RECIPIENT_EMAIL)
            {
                controllerView.displayWarning("There has been a problem with sending the query response." +
                " Please try again later.\n");            
            }
            if (statusCode == controllerEmail.STATUS_UNKNOWN_ERROR) 
            {
                controllerView.displayWarning("There has been a problem with sending the query response." +
                        " Please try again later.\n");
            }
            Collection<Inquiry> queries = controllerContext.getUserInquiry();
            queries.remove(inquiry);
            controllerContext.setUserInquiry(queries);
        }
        else if (!result.equals("b")) {
            controllerView.displayWarning("Please enter a valid input\n");
            respondToInquiry(inquiry);
        }
    
    }
}