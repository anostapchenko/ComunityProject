package com.javamentor.qa.platform.webapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("deleteThisController")
public class TestController {

    @GetMapping("/testpage")
    public String getTestPage(){
        return "testPage";
    }
}
