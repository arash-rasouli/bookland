package org.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginForm {
    @NotBlank(message = "userEmail can not be blank")
    @Email(message = "Wrong userEmail format")
    String userEmail;
    @NotBlank(message = "Password can not be blank")
    @Size(min = 4, message = "Password should contain at least 4 character")
    String password;


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
}
