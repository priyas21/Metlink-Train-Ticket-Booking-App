package nz.ac.app.metlink;

/**
 * Created by 21600481 on 21-10-2016.
 */
public class LoginDetails {
    private String  Email,Password;
    public LoginDetails(String Email, String Password)
    {

        this.setEmail(Email);
        this.setPassword(Password);
    }


    public String gerEmail()
    {

        return Email;
    }

    public String getPassword() {
        return Password;
    }
    public void setEmail(String Email)
    {
        this.Email=Email;
    }
    public void setPassword(String Password) {
    this.Password=Password;
    }


}

