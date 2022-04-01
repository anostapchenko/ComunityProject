package com.javamentor.qa.platform.models.dto;

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
public class UserProfileQuestionDto {
    private Long questionId;
    private String title;
    private List<TagDto> listTagDto;
    private Long countAnswer;
    private LocalDateTime persistDateTime;
}
