package com.nuri.nuribackend.controller;

import com.nuri.nuribackend.domain.User;
import com.nuri.nuribackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {


    @GetMapping("/hello")
    public String hello(){
        return "Hello, Spring!";
    }
}
