package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.question.TagDTO;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.TagDTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TagResourceController {

    private TagDTOService tagDTOServiceImpl;

    @Autowired
    public void setTagServiceImpl(TagDTOService tagDTOServiceImpl) {
        this.tagDTOServiceImpl = tagDTOServiceImpl;
    }

    @GetMapping(path = "/api/user/tag/tracked")
    public @ResponseBody List<TagDTO> getTagDto(Authentication authentication) {
        User currentUser = (User)authentication.getPrincipal();
        return tagDTOServiceImpl.getTagsDTOByUserIdFromTrackedTag(currentUser.getId());
    }
}
