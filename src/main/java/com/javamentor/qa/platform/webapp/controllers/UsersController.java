package com.javamentor.qa.platform.webapp.controllers;

import com.javamentor.qa.platform.service.abstracts.model.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {

    private final UserService userService;

    @GetMapping
    public String getUsers() {
        return "users";
    }
}
