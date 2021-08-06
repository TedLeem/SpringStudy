package com.example.usersservice.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class UserController {


    Environment env;

    @Autowired
    public UserController(Environment env) {
        this.env = env;
    }

    @GetMapping("/health_check")
    public String status(){
        return env.getProperty("greeting.message");
    }
}
