package controller;

import external.AuthenticationService;
import external.EmailService;
import model.SharedContext;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import view.View;

class GuestController extends Controller{
 
    public GuestController(SharedContext context, View view, AuthenticationService authenticationService, EmailService emailService) {
        this.controllerView = view;
        this.controllerContext = context;
        this.controllerAuth = authenticationService;
        this.controllerEmail = emailService;
    }

    public void login()
    {
        controllerView.displayInfo("Login\n");
        String username = controllerView.getInput("Username: ");
        String password = controllerView.getInput("Password: ");
        String loginOutput = controllerAuth.login(username, password);
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(loginOutput);

            if(jsonObject.containsKey("error")) {
                controllerView.displayWarning("Wrong username or password");
            }
            else{
                controllerContext.setCurrentUser((String) jsonObject.get("role"),
                        (String) jsonObject.get("email"));
            }

            } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void logout() {

    }

}