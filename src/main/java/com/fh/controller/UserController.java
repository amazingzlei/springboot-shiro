package com.fh.controller;

import com.fh.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

    @Autowired
    private IUserService userService;

    @RequestMapping("/addUser")
    @ResponseBody
    private void addUser(){
        userService.addUser();
    }

    @RequestMapping("/updateUser")
    @ResponseBody
    private void updateUser(){
        userService.updateUser();
    }

    @RequestMapping("/delUser")
    @ResponseBody
    private void delUser(){
        userService.delUser();
    }
}
