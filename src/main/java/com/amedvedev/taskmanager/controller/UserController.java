package com.amedvedev.taskmanager.controller;

import com.amedvedev.taskmanager.service.UserService;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
}
