package com.javamentor.qa.platform.models.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TagDto {

   private Long id;
   private String name;
}

