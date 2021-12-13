package com.javamentor.qa.platform.webapp.controllers.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @RequestMapping("/home")
    public String greeting(){
        return "Hello";
    }
}
