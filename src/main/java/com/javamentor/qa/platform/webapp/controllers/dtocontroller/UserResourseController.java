package com.javamentor.qa.platform.webapp.controllers.dtocontroller;

import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.UserDtoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/user/")
public class UserResourseController {
    final private UserDtoService userDtoService;

    public UserResourseController(UserDtoService userDtoService) {
        this.userDtoService = userDtoService;
    }

    @GetMapping("{userId}")
    public ResponseEntity<List<UserDto>> getUser (@PathVariable("userId") long id) {
        List<UserDto> userDto = userDtoService.findCategoryByName(id);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}
