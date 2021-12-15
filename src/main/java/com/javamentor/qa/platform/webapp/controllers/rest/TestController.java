package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.service.impl.model.TestServiceNumberOfUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    //Используем временный тестовый класс для получения количества пользователей в БД
    private final
    TestServiceNumberOfUsers testService;

    public TestController(TestServiceNumberOfUsers testService) {
        this.testService = testService;
    }

    @GetMapping("api/numberofusers")
    public ResponseEntity<Object> allUser(){
        int size = testService.getNumberOfUsers();
        if (size !=0 ){
            return new ResponseEntity<>(size, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No users in database", HttpStatus.NOT_FOUND);
        }
    }
}