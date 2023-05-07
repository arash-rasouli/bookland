package org.example.system;

import org.example.dao.UserRepository;
import org.example.exceptions.*;
import org.example.model.*;
import org.example.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BooklandSystem {

    @Value("${bookland.jwt.key}")
    private String JWTKey;

    @Autowired
    UserRepository userRepo;

    public String login(String userEmail, String password) throws Exception{
        User foundedUser = userRepo.findByEmail(userEmail);
        String passHash = Util.hexToString(Util.getSHA(password));

        if (foundedUser == null) {
            throw new NotFound("Unregistered User. Please signup first");
        }
        if (!foundedUser.getPassword().equals(passHash)) {
            throw new RuntimeException("Invalid Credentials");

        }

        return Util.createJWT(userEmail, JWTKey);
    }

}
