package controller;

import external.AuthenticationService;
import external.EmailService;
import model.SharedContext;
import view.View;

public class MenuController extends Controller {

    Controller currentController;
    public MenuController(SharedContext context, View view, AuthenticationService authenticationService, EmailService emailService)
    {
        this.controllerAuth = authenticationService;
        this.controllerContext = context;
        this.controllerEmail = emailService;
        this.controllerView = view;

        currentController = new GuestController(context, view, authenticationService, emailService);
    }

    @Override
    protected void login() {}

    @Override
    protected void logout() {

    }

    protected int selectFromMenu(String[] options, String returnOption)
    {
        int temp_count = 0;
        String temp_string;
        controllerView.displayDivider();
        for(String item : options)
        {
            temp_count+= 1;
            temp_string = String.format("%d.", temp_count);
            controllerView.displayInfo(temp_string);
            controllerView.displayInfo(item + "\n");
        }
        if(!returnOption.isEmpty())
        {
            controllerView.displayInfo(String.format("%d.", ++temp_count));
            controllerView.displayInfo(returnOption + "\n");
        }
        String result = controllerView.getInput(String.format("Please choose a number(from 1 to %d) " +
                "to select the option: ", temp_count));
        controllerView.displayDivider();
        int result_int;
        if(!result.matches("\\d+"))
        {
            controllerView.displayError("Please enter a valid number");
            result_int = selectFromMenu(options, returnOption);
        }
        else
        {
            result_int = Integer.parseInt(result);
            if ((result_int <= 0) || (result_int > temp_count)) {
                controllerView.displayError("Please enter a valid number");
                result_int = selectFromMenu(options, returnOption);
            }
        }
        return result_int;
    }

    public void mainMenu()
    {
        String role = controllerContext.getUserRoleContext();
        boolean output;
        if(role.equals("Guest"))
        {
            currentController = new InquirerController(controllerContext, controllerView,
                    controllerAuth, controllerEmail);
            output = handleGuestMainMenu();
            if(output)
                mainMenu();
        }
        else if(role.equals("Student"))
        {
            currentController = new InquirerController(controllerContext, controllerView,
                    controllerAuth, controllerEmail);
            output = handleStudentMainMenu();
            if(output)
                mainMenu();
        }
        else if(role.equals("TeachingStaff"))
        {
            currentController = new TeachingStaffController(controllerContext, controllerView,
                    controllerAuth, controllerEmail);
            output = handleTeachingStaffMainMenu();
            if(output)
                mainMenu();
        }
        else if(role.equals("AdminStaff"))
        {
            currentController = new AdminStaffController(controllerContext, controllerView,
                    controllerAuth, controllerEmail);
            output = handleAdminStaffMainMenu();
            if(output)
                mainMenu();
        }
    }


    private boolean handleGuestMainMenu() {
        GuestMainMenuOption[] menuOptions = GuestMainMenuOption.values();
        String[] guestOptions = new String[menuOptions.length];
        int i_count_main = 0;
        for(GuestMainMenuOption element: menuOptions)
            guestOptions[i_count_main++] = element.toString();
        int input = selectFromMenu(guestOptions, "");
        switch (input)
        {
            case 1:
                currentController = new GuestController(controllerContext, controllerView,
                        controllerAuth, controllerEmail);
                    currentController.login();
                     return true;
            case 2://display FAQs
                currentController.consultFAQ();
                return true;
            case 3://display Webpages
                currentController.searchPages();
                return true;
            case 4://display Inquiry Asking section
                currentController.contactStaff();
                return true;
        };
        return false;
    }

    private boolean handleStudentMainMenu() {
        StudentMainMenuOption[] menuOptions = StudentMainMenuOption.values();
        String[] studentOptions = new String[menuOptions.length];
        int i = 0;
        for(StudentMainMenuOption element: menuOptions)
            studentOptions[i++] = element.toString();
        int input = selectFromMenu(studentOptions, "");
        switch (input)
        {
            case 1://Logout
                currentController = new AuthenticatedUserController(controllerContext, controllerView,
                        controllerAuth, controllerEmail);
                currentController.logout();
                return true;
            case 2://consult FAQs
                currentController.consultFAQ();
            case 3://consult webpage
                currentController.searchPages();
            case 4://Inquiry Asking
                currentController.contactStaff();
        }
        return false;
    }

    private boolean handleTeachingStaffMainMenu() {
        TeachingStaffMainMenuOption[] menuOptions = TeachingStaffMainMenuOption.values();
        String[] tsOptions = new String[menuOptions.length];
        int i = 0;
        for(TeachingStaffMainMenuOption element: menuOptions)
            tsOptions[i++] = element.toString();
        int input = selectFromMenu(tsOptions, "");
        switch (input)
        {
            case 1://Logout
                currentController = new AuthenticatedUserController(controllerContext, controllerView,
                        controllerAuth, controllerEmail);
                currentController.logout();
                return true;
            case 2://View Queries
                currentController.manageReceivedInquiries();
                return true;
        };
        return false;
    }

    private boolean handleAdminStaffMainMenu()
    {
        AdminStaffMainMenuOption[] menuOptions = AdminStaffMainMenuOption.values();
        String[] adminOptions = new String[menuOptions.length];
        int i = 0;
        for(AdminStaffMainMenuOption element: menuOptions)
            adminOptions[i++] = element.toString();
        int input = selectFromMenu(adminOptions, "");

        switch (input)
        {
            case 1://Logout
                currentController = new AuthenticatedUserController(controllerContext, controllerView,
                        controllerAuth, controllerEmail);
                currentController.logout();
                return true;
            case 2://Manage Queries
                currentController.manageInquiries();
                return true;
            case 3://Add Page
                currentController.addPage();
                return true;
            case 4://See All Pages
                currentController.viewAllPages();
                return true;
            case 5://Manage FAQs
                currentController.manageFAQ();
                return true;
        };
        return false;
    }
}