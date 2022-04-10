package com.qa.platform.models.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionViewDto {
    private Long id;
    private String title;
    private Long authorId;
    private Long authorReputation; //(можно подсчитать с помощью sql);
    private String authorName;
    private String authorImage;
    private String description;
    private int viewCount; //(пока не считай это поле, как оно будет считаться решим позже, пусть пока будет 0)
    private int countAnswer;// (можно подсчитать с помощью sql);
    private int countValuable; // (Это голоса за ответ QuestionVote);
    private LocalDateTime persistDateTime;
    private LocalDateTime lastUpdateDateTime;
    private List<TagDto> listTagDto;
}
