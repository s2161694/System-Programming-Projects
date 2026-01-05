package controller;

import external.AuthenticationService;
import external.EmailService;
import model.Inquiry;
import model.SharedContext;
import view.View;

import java.util.Collection;

class TeachingStaffController extends StaffController {
    public TeachingStaffController(SharedContext context, View view, AuthenticationService authenticationService,EmailService emailService){
        super(context, view, authenticationService, emailService);
    }
    public void manageReceivedInquiries()
    {
        Collection<String> inquiryTitles = getInquiryTitles(controllerContext.getUserInquiry());
        int temp_count = 0;
        String temp_string;
        for(String e: inquiryTitles)
        {
            temp_count+= 1;
            temp_string = String.format("%d.", temp_count);
            controllerView.displayInfo(temp_string);
            controllerView.displayInfo(e + "\n");
        }
        String result = controllerView.getInput(String.format("Please choose a number(from 1 to %d) " +
                "to select the option(or to exit enter e): ", temp_count));
        if(result.equals("e"))
        {}
        else if(result.matches("\\d+"))
        {
            int result_int;
                result_int = Integer.parseInt(result);
                if ((result_int <= 0) || (result_int > temp_count)) {
                    controllerView.displayWarning("Please enter a valid number\n");
                    manageReceivedInquiries();
                }
                else
                {
                    temp_count = 0;
                    for(Inquiry query: controllerContext.getUserInquiry())
                    {
                        if(temp_count++ == result_int)
                        {
                            respondToInquiry(query);
                        }
                    }
                    manageReceivedInquiries();
                }
        }
        else {
            controllerView.displayWarning("Please enter a valid number\n");
            manageReceivedInquiries();
        }
    }

}