package com.javamentor.qa.platform.webapp.controllers.rest;

import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@RestController
public class AnswerResourceController {

    private List<Long> ids = Arrays.asList(1L, 2L, 3L);


    @PostMapping(path = "api/user/question/{questionId}/answer/{id}/upVote")
    public @ResponseBody int upVote(@PathVariable long id, Principal principal) {

        return 0;
    }

    @PostMapping(path = "api/user/question/{questionId}/answer/{id}/downVote")
    public @ResponseBody int downVote(@PathVariable long id, Principal principal) {
        return 0;
    }

    private boolean userChecking(Long id) {
        return true;
    }
}
