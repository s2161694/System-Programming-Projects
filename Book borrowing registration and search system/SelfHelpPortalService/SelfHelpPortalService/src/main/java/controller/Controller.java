package controller;


import external.AuthenticationService;
import external.EmailService;
import view.View;
import model.SharedContext;
import java.util.Collection;


public abstract class Controller {

    SharedContext controllerContext;
    EmailService controllerEmail;
    AuthenticationService controllerAuth;
    View controllerView;

    protected Controller(SharedContext context, View view, AuthenticationService authenticationService, EmailService emailService) {

    }

    public Controller() {

    }



    protected <T> void selectFromMenu(Collection<T> options, String returnOption){

        return;
    }

    protected abstract void login();

    protected abstract void logout();

    protected void manageReceivedInquiries() {
    }

    protected void manageInquiries() {
    }

    protected void addPage() {
    }

    protected void viewAllPages() {
    }

    protected void manageFAQ() {
    }

    protected void consultFAQ() {
    }

    protected void searchPages() {
    }

    protected void contactStaff() {
    }
}
