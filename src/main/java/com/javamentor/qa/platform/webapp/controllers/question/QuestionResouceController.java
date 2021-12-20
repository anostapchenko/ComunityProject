package com.javamentor.qa.platform.webapp.controllers.question;
import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user/question/")
public class QuestionResouceController {
    @Autowired
    private QuestionService questionService;

    @GetMapping("count")
    public ResponseEntity<List<Question>> getQuestionCount() {
        List<Question> question = questionService.getCountQuest();
        return new ResponseEntity<>(question, HttpStatus.OK);
    }
}
