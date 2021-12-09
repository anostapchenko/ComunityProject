package com.javamentor.qa.platform.webapp.controllers.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {
    @RequestMapping("/home")
    public @ResponseBody String greeting(){
        return "Hello";
    }
}
