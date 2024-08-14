package com.steparrik.dto.comment;

import com.steparrik.dto.user.UserResponseDto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommentResponseDto {
    private Long id;
    private String text;
    private LocalDateTime timestamp;
    private UserResponseDto sender;
}
