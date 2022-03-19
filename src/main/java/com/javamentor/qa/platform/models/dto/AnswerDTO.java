package com.javamentor.qa.platform.models.dto;

import com.javamentor.qa.platform.models.entity.question.answer.CommentAnswer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDTO {

    private Long id;
    private Long userId; //+++
    private Long userReputation; //---
    private Long questionId; //+++
    private String htmlBody;
    private LocalDateTime persistDateTime;
    private Boolean isHelpful;
    private LocalDateTime dateAccept; //+++
    private Long countValuable; //---
    private String image; //---
    private String nickName; //---

//    private LocalDateTime updateDateTime;--------------
//    private Boolean isDeleted;
//    private Boolean isDeletedByModerator;

}
