package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.impl.SetVoteReputationOfQuestionService;
import com.javamentor.qa.platform.service.impl.model.TestServiceNumberOfUsers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Question Resource Controller", description = "Управление сущностями, которые связаны с вопросами")
public class QuestionResourceController {

    @Autowired
    SetVoteReputationOfQuestionService setVoteReputationOfQuestionService;

    @PostMapping("api/user/question/{id}/upVote")
    @Operation(
            summary = "Голосование ЗА вопрос",
            description = "Устанавливает голос +1 за вопрос и +10 к репутации автора вопроса"
    )
    public ResponseEntity<?> upVote(@PathVariable("id") Long id) {
//        Заготовка когда появится секьюрити
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        User user =(User)auth.getPrincipal();
//        Long userId = user.getId();
        Long userId = 1L;
        int countUpVote = 10;
        if (setVoteReputationOfQuestionService.validateQuestion(id)) return new ResponseEntity<>("Can't find question with id:"+ id , HttpStatus.NOT_FOUND);
        if (setVoteReputationOfQuestionService.validateUserVote(id, userId)) {
            setVoteReputationOfQuestionService.setVote(userId, id, VoteType.UP_VOTE);
            setVoteReputationOfQuestionService.setReputation(userId, id, countUpVote);
        }
        return new ResponseEntity<>((long) setVoteReputationOfQuestionService.getVote(id), HttpStatus.OK);
    }

    @PostMapping("api/user/question/{id}/downVote")
    @Operation(
            summary = "Голосование ПРОТИВ вопроса",
            description = "Устанавливает голос -1 за вопрос и -5 к репутации автора вопроса"
    )
    public ResponseEntity<?> downVote(@PathVariable("id") Long id) {
//        Заготовка когда появится секьюрити
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        User user =(User)auth.getPrincipal();
//        Long userId = user.getId();
        Long userId = 1L;
        int countDownVote = -5;
        if (setVoteReputationOfQuestionService.validateQuestion(id)) return new ResponseEntity<>("Can't find question with id:"+ id , HttpStatus.NOT_FOUND);
        if (setVoteReputationOfQuestionService.validateUserVote(id, userId)) {
            setVoteReputationOfQuestionService.setVote(userId, id, VoteType.DOWN_VOTE);
            setVoteReputationOfQuestionService.setReputation(userId, id, countDownVote);
        }
        return new ResponseEntity<>((long) setVoteReputationOfQuestionService.getVote(id), HttpStatus.OK);
    }
}

