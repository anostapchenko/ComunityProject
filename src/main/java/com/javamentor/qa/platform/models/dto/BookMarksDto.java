package com.javamentor.qa.platform.models.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookMarksDto {
    private Long questionId;
    private String title;
    private List<TagDto> tagDtoList;
    private Long countAnswer;
    private Long countVote;
    private Long countView;
    private LocalDateTime persistQuestionDate;
}
