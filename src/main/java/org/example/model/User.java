package org.example.model;

import jakarta.persistence.*;

import jakarta.persistence.Table;
import org.example.utils.Util;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "USER_")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String email;
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
