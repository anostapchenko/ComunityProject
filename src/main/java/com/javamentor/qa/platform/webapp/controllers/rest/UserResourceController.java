package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.dao.impl.pagination.UserPageDtoDaoAllUsersImpl;
import com.javamentor.qa.platform.models.dto.PageDTO;
import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.models.entity.pagination.PaginationData;
import com.javamentor.qa.platform.service.abstracts.pagination.UserPageDtoService;
import com.javamentor.qa.platform.service.impl.pagination.UserDtoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserResourceController {


    private final UserPageDtoService userDtoService;

    @Autowired
    public UserResourceController(UserPageDtoService userDtoService) {
        this.userDtoService = userDtoService;
    }



    @GetMapping("/api/user/new")
    public ResponseEntity<PageDTO<UserDto>> paginationById(@RequestParam int page, @RequestParam(defaultValue = "10") int items) {
        PaginationData data = new PaginationData(page, items,
                UserPageDtoDaoAllUsersImpl.class.getSimpleName());
        return new ResponseEntity<>(userDtoService.getPageDto(data), HttpStatus.OK);

    }
}
