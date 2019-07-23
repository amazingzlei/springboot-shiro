package com.fh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyViewController {
    @RequestMapping("/login")
    public String gotoLogin(){
        return "/login";
    }

    @RequestMapping("/unauthorized")
    public String gotoUnauthorized(){
        return "/unauthorized";
    }

    @RequestMapping("/user")
    public String gotoUser(){
        return "/user";
    }

    @RequestMapping("/admin")
    public String gotoAdmin(){
        return "/admin";
    }

    @RequestMapping("/list")
    public String gotoList(){
        return "/list";
    }
}
