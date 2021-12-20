package com.javamentor.qa.platform.webapp.controllers.question;
import com.javamentor.qa.platform.models.dto.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user/")
public class QuestionResouceController {

//    @GetMapping("question/count")
//    public ResponseEntity<List<UserDto>> getQuestionCount() {
//        return new ResponseEntity<>(userDto, HttpStatus.OK);
//    }
}
