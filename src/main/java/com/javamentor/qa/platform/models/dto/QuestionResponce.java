package com.javamentor.qa.platform.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponce {
    @Schema(description = "Ответ на вопрос", example = "{\"id\":10,\"title\":\"Question 10\",\"authorId\":8,\"authorReputation\":800,\"authorName\":\"User 8\",\"authorImage\":\"/images/noUserAvatar.png\",\"description\":\"What you think about question 10?\",\"viewCount\":0,\"countAnswer\":5,\"countValuable\":1,\"persistDateTime\":\"2021-12-23T12:29:51.030441\",\"lastUpdateDateTime\":\"2021-12-23T12:29:51.030441\",\"listTagDto\":[{\"id\":1,\"name\":\"Tag 1\",\"description\":\"Description of tag 1\"},{\"id\":4,\"name\":\"Tag 4\",\"description\":\"Description of tag 4\"}]}")
    private String question;
}
