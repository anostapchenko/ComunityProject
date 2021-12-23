package com.javamentor.qa.platform.models.dto.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagDTO {

    private Long id;
    private String name;
    private LocalDateTime persistDateTime;
}
