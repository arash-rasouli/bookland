package org.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.example.model.User;

public class SignupForm {
    @NotBlank(message = "userEmail can not be blank")
    @Email(message = "Wrong userEmail format")
    String userEmail;
    @NotBlank(message = "Password can not be blank")
    @Size(min = 4, message = "Password should contain at least 4 character")
    String password;
    @NotBlank(message = "Repeated password can not be blank")
    String repeatedPassword;

    public User initializeUser(){
        return new User(userEmail, password);
    }


    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatedPassword() {
        return repeatedPassword;
    }

    public void setRepeatedPassword(String repeatedPassword) {
        this.repeatedPassword = repeatedPassword;
    }
}
