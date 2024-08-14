package com.steparrik.dto.task;

import com.steparrik.dto.user.UserResponseDto;
import com.steparrik.entity.enums.Priority;
import com.steparrik.entity.enums.Status;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaskEditDto {
    private String title;

    private String description;

    private Status status;

    private Priority priority;

    private UserResponseDto executor;
}
