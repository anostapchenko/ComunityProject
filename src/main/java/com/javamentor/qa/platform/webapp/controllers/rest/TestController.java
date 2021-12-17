package com.javamentor.qa.platform.webapp.controllers.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.impl.model.TestServiceNumberOfUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    //Используем временный тестовый класс для получения количества пользователей в БД
    private final
    TestServiceNumberOfUsers testService;

    public TestController(TestServiceNumberOfUsers testService) {
        this.testService = testService;
    }

    @GetMapping("api/numberofusers")
    public ResponseEntity<Object> numberOfUsers(){
        int size = testService.getNumberOfUsers();
        if (size !=0 ){
            return new ResponseEntity<>(size, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No users in database", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("api/allusers")
    public List<User> allUsers2(){
        List<User> allUsers = testService.getAllUser2();
        System.out.println(allUsers);
        return allUsers;
    }
}