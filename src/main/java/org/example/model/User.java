package org.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import org.example.utils.Util;

@Entity
public class User {
    @Id
    private long id;

    private String email;
    private String password;

    public boolean verifyPassword(String password){
        String passHash =  Util.hexToString(Util.getSHA(password));
        return this.password.equals(passHash);
    }
}
