package org.example.model;

import jakarta.persistence.*;

import jakarta.persistence.Table;
import org.example.utils.Util;

@Entity
@Table(name = "USER_")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String email;
    private String password;

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
