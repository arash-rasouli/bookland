package org.example.controller;

import jakarta.validation.Valid;

import org.example.dto.ResponseInfo;
import org.example.dto.SignupForm;
import org.example.exceptions.AlreadyExist;
import org.example.exceptions.NotFound;
import org.example.exceptions.NotMatch;
import org.example.model.User;
import org.example.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.AbstractMap;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @PostMapping("/login")
    public ResponseEntity<ResponseInfo> login(@Valid @RequestBody User user) throws NotFound, NotMatch {
        ResponseInfo responseInfo;
        try {
            AbstractMap.SimpleEntry<String, String> jwt = authService.login(user);
            responseInfo = new ResponseInfo(jwt, true, "Login Successfully");
            logger.info("Successful login by username = {}",user.getEmail());
            return new ResponseEntity<>(responseInfo, HttpStatus.OK);
        }catch (Exception ex){
            logger.info("Failed login by username = {}",user.getEmail());
            if (ex instanceof NotFound){throw (NotFound) ex;}
            if (ex instanceof NotMatch) {throw (NotMatch) ex;}

            responseInfo = new ResponseInfo(null, false, "Internal Server Error");
            logger.error("Unhandled Exception: error message : {} , error stack : {}", ex.getMessage(), ex.getStackTrace());
            return new ResponseEntity<>(responseInfo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseInfo> signup(@Valid @RequestBody SignupForm signupForm) throws AlreadyExist, NotMatch {
        ResponseInfo responseInfo;
        try {
            AbstractMap.SimpleEntry<String, String> jwt = authService.signup(signupForm);
            responseInfo = new ResponseInfo(jwt, true, "Signup Successfully");
            logger.info("Successful signup by username = {}", signupForm.getUserEmail());
            return new ResponseEntity<>(responseInfo, HttpStatus.OK);
        }catch (Exception ex){
            logger.info("Failed signup by username = {}", signupForm.getUserEmail());
            if (ex instanceof AlreadyExist){throw (AlreadyExist) ex;}
            if (ex instanceof NotMatch) {throw (NotMatch) ex;}

            responseInfo = new ResponseInfo(null, false, "Internal Server Error");
            logger.error("Unhandled Exception: error message : {} , error stack : {}", ex.getMessage(), ex.getStackTrace());
            return new ResponseEntity<>(responseInfo, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
