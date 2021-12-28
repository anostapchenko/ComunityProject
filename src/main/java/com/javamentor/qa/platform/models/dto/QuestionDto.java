package com.javamentor.qa.platform.models.dto;

import com.javamentor.qa.platform.models.dto.question.TagDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto {
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
