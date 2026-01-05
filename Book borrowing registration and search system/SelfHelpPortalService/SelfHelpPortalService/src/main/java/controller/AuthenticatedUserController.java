package controller;

import external.AuthenticationService;
import external.EmailService;
import model.SharedContext;
import view.View;

class AuthenticatedUserController extends Controller {
    public AuthenticatedUserController(SharedContext context, View view, AuthenticationService authenticationService, EmailService emailService)
    {
        this.controllerContext = context;
        this.controllerView = view;
        this.controllerAuth = authenticationService;
        this.controllerEmail = emailService;
    }

    @Override
    protected void login() {}

    public void logout() {
        boolean userConfirmation = controllerView.getYesNoInput("Are you sure you want to logout?");
        if(userConfirmation)
            controllerContext.setCurrentUser("", "");
    }
}