package model;

public class AuthenticatedUser extends User {
    private String email;
    private String role;

    public AuthenticatedUser(String role, String email)
    {
        this.role = role;
        this.email = email;
    }

    public String getUserRole()
    {
        return this.role;
    }
   
    public String getEmail() {
        return this.email;
    }

    public void setRole(String role_param)
    {
        this.role = role_param;
    }
    public void setEmail(String email_param)
    {
        this.email = email_param;
    }
}