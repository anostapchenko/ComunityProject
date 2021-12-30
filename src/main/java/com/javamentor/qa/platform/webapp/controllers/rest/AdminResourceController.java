package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.service.abstracts.model.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/")
public class AdminResourceController {

    private final UserService userService;

    @Autowired
    public AdminResourceController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/disable-user/{id}")
    public ResponseEntity<Object> deleteUserWithId(@PathVariable("id") long id) {
        userService.deleteById(id);
        return new ResponseEntity<>("User with id = " + id + " was successfully disabled", HttpStatus.OK);
    }
}
