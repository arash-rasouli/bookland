package org.example.controller;

import org.example.BooklandApp;
import org.example.dao.UserRepository;
import org.example.dto.ResponseInfo;
import org.example.model.User;
import org.example.system.BooklandSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.AbstractMap.*;


@RestController
@RequestMapping("/auth")
public class UserController {

//    @Autowired
    BooklandSystem system;

    public UserController(BooklandSystem system) {
        this.system = system;
    }

    public void setSystem(BooklandSystem system) {
        this.system = system;
    }

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @PostMapping("/login")
    @ResponseBody
    public Object login(@RequestParam(value = "userEmail") String userEmail,
                        @RequestParam(value = "password") String password){
        try {
            SimpleEntry<String, Object> jwt = system.login(userEmail, password);
            ResponseInfo responseInfo = new ResponseInfo(jwt, true, "Login Successfully");
            return new ResponseEntity<>(responseInfo, HttpStatus.OK);
        }catch (Exception e){
            logger.info("Falied login by username = {}",userEmail);
            ResponseInfo responseInfo = new ResponseInfo(null, false, "Login Failed");
            responseInfo.addError(e.getMessage());
            return new ResponseEntity<>(responseInfo, HttpStatus.UNAUTHORIZED);
        }
    }
}
