package org.example.model;

import jakarta.persistence.*;

import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.example.utils.Util;

import java.util.*;

@Entity
@Table(name = "USER_")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotBlank(message = "userEmail can not be blank")
    @Email(message = "Wrong email format")
    private String email;
    @Size(min = 4, message = "Password should contain at least 4 character")
    private String password;

    @ManyToMany
    @JoinTable(
            name = "user_book",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    List<Book> library;

    public boolean verifyPassword(String password){
        String passHash =  Util.hexToString(Util.getSHA(password));
        return this.password.equals(passHash);
    }

    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
}
