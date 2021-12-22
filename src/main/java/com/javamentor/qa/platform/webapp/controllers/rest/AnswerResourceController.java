package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.VoteAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class AnswerResourceController {

    private VoteAnswerService voteAnswerServiceImpl;

    @Autowired
    public void setVoteAnswerServiceImpl(VoteAnswerService voteAnswerServiceImpl) {
        this.voteAnswerServiceImpl = voteAnswerServiceImpl;
    }

    @PostMapping(path = "api/user/question/{questionId}/answer/{id}/upVote")
    public @ResponseBody long upVote(@PathVariable(name = "id") long answerId,
                Authentication authentication) {
        User currentUser = (User)authentication.getPrincipal();
        return upDownVoteEvent(VoteType.UP_VOTE, answerId, currentUser);
    }

    @PostMapping(path = "api/user/question/{questionId}/answer/{id}/downVote")
    public @ResponseBody long downVote(@PathVariable(name = "id") long answerId,
                  Authentication authentication) {
        User currentUser = (User)authentication.getPrincipal();
        return upDownVoteEvent(VoteType.DOWN_VOTE, answerId, currentUser);
    }

    private long upDownVoteEvent(VoteType vote, long answerId, User currentUser) {
        if (!voteAnswerServiceImpl.existsVoteAnswerByAnswerIdAndUserId(answerId, currentUser.getId())) {
            voteAnswerServiceImpl.addVoteAnswer(vote, answerId, currentUser);
        } else {
            VoteAnswer voteAnswer = voteAnswerServiceImpl.getVoteAnswerByAnswerIdAndUserId(answerId, currentUser.getId());
            if (!voteAnswer.getVote().equals(vote)) {
                voteAnswer.setVote(vote);
                voteAnswerServiceImpl.updateVoteAnswer(voteAnswer, answerId, currentUser.getId());
            }
        }
        return voteAnswerServiceImpl.getVoteCount(answerId);
    }
}
