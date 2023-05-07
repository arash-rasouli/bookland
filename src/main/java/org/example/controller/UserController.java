package org.example.controller;

import org.example.BooklandApp;
import org.example.dao.UserRepository;
import org.example.model.User;
import org.example.system.BooklandSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    BooklandSystem system;


    @PostMapping("/login")
    @ResponseBody
    public String login(@RequestParam(value = "userEmail") String userEmail,
                        @RequestParam(value = "password") String password){
        System.out.println("hello");
        try {
            return system.login(userEmail, password);
        }catch (Exception e){
            return  e.getMessage();
        }
    }
}
