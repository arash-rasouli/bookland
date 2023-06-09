package org.example.service;

import org.example.dao.UserRepository;
import org.example.dto.LoginForm;
import org.example.dto.SignupForm;
import org.example.exceptions.AlreadyExist;
import org.example.exceptions.NotFound;
import org.example.exceptions.NotMatch;
import org.example.model.User;
import org.example.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.AbstractMap.SimpleEntry;

@Component
public class AuthService {
    private String JWTKey;

    @Autowired
    UserRepository userRepo;

    public AuthService() {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        try {
            JWTKey = Util.getJWTKey();
            if(JWTKey == null){
                logger.error("[AuthService] Unable to find bookland.jwt.key in secrets.properties");
            }
        }
        catch (Exception ex){
            logger.error("[AuthService] Unable to read secrets.properties");
        }
    }

    public SimpleEntry<String, String> login(LoginForm loginForm) throws Exception{
        User foundedUser = userRepo.findByEmail(loginForm.getUserEmail());

        if (foundedUser == null) {
            throw new NotFound("Unregistered User. Please signup first");
        }
        if (!foundedUser.verifyPassword(loginForm.getPassword())) {
            throw new NotMatch("Invalid Credentials");
        }

        return new SimpleEntry<>("jwt", Util.createJWT(loginForm.getUserEmail(), JWTKey));
    }

    public SimpleEntry<String, String> signup(SignupForm signupForm) throws Exception{
        User foundedUser = userRepo.findByEmail(signupForm.getUserEmail());
        if (foundedUser != null) {
            throw new AlreadyExist("This userEmail is already used. Please try another userEmail");
        }
        if(!signupForm.getPassword().equals(signupForm.getRepeatedPassword())){
            throw new NotMatch("Password doesn't match with repeated password");
        }

        User newUser = signupForm.initializeUser();
        String hashPassword = Util.hexToString(Util.getSHA(signupForm.getPassword()));
        newUser.setPassword(hashPassword);

        userRepo.save(newUser);

        return new SimpleEntry<>("jwt", Util.createJWT(signupForm.getUserEmail(), JWTKey));

    }
}
