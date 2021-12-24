package com.javamentor.qa.platform.models.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class TagDto implements Serializable {

    private Long id;
    private String name;
    private String description;

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name=" + name +
                ", description=" + description +
                '}';
    }
}