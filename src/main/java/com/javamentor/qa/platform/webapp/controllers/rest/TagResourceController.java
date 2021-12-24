package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.TagDTO;
import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TagResourceController {

//    @GetMapping ("/api/user/tag/ignored")
//    public ResponseEntity<List<TagDTO>> getIgnoredTag (){
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        User user =(User)auth.getPrincipal();
//        Long userId = user.getId();
//
//        return new ResponseEntity<List<TagDTO>>(ListTagDtoByIgnoredTag, HttpStatus.OK);
//    }
}
