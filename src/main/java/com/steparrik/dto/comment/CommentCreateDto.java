package com.steparrik.dto.comment;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommentCreateDto {

    @NotEmpty(message = "Text cannot be empty")
    private String text;
}
