package com.steparrik.dto.task;

import com.steparrik.dto.comment.CommentResponseDto;
import com.steparrik.dto.user.UserResponseDto;
import com.steparrik.entity.enums.Priority;
import com.steparrik.entity.enums.Status;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaskResponseDto {
    @NotEmpty(message = "Id cannot be null")
    private Long id;

    @NotEmpty(message = "Title cannot be empty")
    private String title;

    @NotEmpty(message = "Description cannot be empty")
    private String description;

    @NotNull(message = "Status cannot be null")
    private Status status;

    @NotNull(message = "Priority cannot be mull")
    private Priority priority;

    @NotNull(message = "Timestamp cannot be null")
    private LocalDateTime timestamp;

    private UserResponseDto owner;

    private UserResponseDto executor;

    private List<CommentResponseDto> comments;

}
