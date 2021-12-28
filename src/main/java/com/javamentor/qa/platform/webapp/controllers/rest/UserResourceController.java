package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.service.abstracts.model.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserResourceController {

    private final UserService userService;

    @Autowired
    public UserResourceController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/disable-user/{email}")
    public void disableUserWithEmail(@PathVariable("email") String email) {
        userService.disableUserWithEmail(email);
    }

}
