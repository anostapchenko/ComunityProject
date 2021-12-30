package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.dao.impl.pagination.UserPageDtoDaoByVoteImpl;
import com.javamentor.qa.platform.models.dto.PageDTO;
import com.javamentor.qa.platform.models.dto.user.UserDto;
import com.javamentor.qa.platform.models.entity.pagination.PaginationData;
import com.javamentor.qa.platform.service.abstracts.pagination.UserPageDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserResourceController {

    private UserPageDtoService userDtoService;

    @Autowired
    public UserResourceController(UserPageDtoService userDtoService) {
        this.userDtoService = userDtoService;
    }

    @GetMapping(path = "/api/user/vote")
    public ResponseEntity<PageDTO<UserDto>> getUsersByVoteAsc(@RequestParam int page, @RequestParam int items) {
        PaginationData data = new PaginationData(page, items,
                UserPageDtoDaoByVoteImpl.class.getSimpleName());
        return new ResponseEntity<>(userDtoService.getPageDto(data), HttpStatus.OK);
    }
}
