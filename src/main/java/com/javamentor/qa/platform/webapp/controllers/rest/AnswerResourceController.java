package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import com.javamentor.qa.platform.service.abstracts.model.VoteAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class AnswerResourceController {

    private VoteAnswerService voteAnswerServiceImpl;
    private UserService userServiceImpl;

    @Autowired
    public void setVoteAnswerServiceImpl(VoteAnswerService voteAnswerServiceImpl,
                                         UserService userServiceImpl) {
        this.voteAnswerServiceImpl = voteAnswerServiceImpl;
        this.userServiceImpl = userServiceImpl;
    }

    @PostMapping(path = "api/user/question/{questionId}/answer/{id}/upVote")
    public @ResponseBody
    long upVote(@PathVariable(name = "id") long answerId,
                @PathVariable(name = "questionId") long userId,
                Principal principal) {
        return upDownVoteEvent(VoteType.UP_VOTE, answerId, userId);
    }

    @PostMapping(path = "api/user/question/{questionId}/answer/{id}/downVote")
    public @ResponseBody
    long downVote(@PathVariable(name = "id") long answerId,
                  @PathVariable(name = "questionId") long userId,
                  Principal principal) {
        return upDownVoteEvent(VoteType.DOWN_VOTE, answerId, userId);
    }

    private long upDownVoteEvent(VoteType vote, long answerId, long currentUserId) {
        if (!voteAnswerServiceImpl.existsVoteAnswerByAnswerIdAndUserId(answerId, currentUserId)) {
            voteAnswerServiceImpl.addVoteAnswer(vote, answerId, userServiceImpl.getById(currentUserId).get());
        } else {
            VoteAnswer voteAnswer = voteAnswerServiceImpl.getVoteAnswerByAnswerIdAndUserId(answerId, currentUserId);
            if (!voteAnswer.getVote().equals(vote)) {
                voteAnswer.setVote(vote);
                voteAnswerServiceImpl.updateVoteAnswer(voteAnswer, answerId, currentUserId);
            }
        }
        return voteAnswerServiceImpl.getVoteCount(answerId);
    }
}
