package com.steparrik.utils.mapper.task;

import com.steparrik.dto.task.TaskResponseDto;
import com.steparrik.entity.Task;
import com.steparrik.utils.mapper.comment.CommentResponseMapper;
import com.steparrik.utils.mapper.user.UserResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TaskResponseMapper {
    private final UserResponseMapper userResponseMapper;
    private final CommentResponseMapper commentResponseMapper;

    public TaskResponseDto toDto(Task e) {
        if (e == null) {
            return null;
        }
        TaskResponseDto taskResponseDto = new TaskResponseDto();
        taskResponseDto.setId(e.getId());
        taskResponseDto.setTitle(e.getTitle());
        taskResponseDto.setDescription(e.getDescription());
        taskResponseDto.setStatus(e.getStatus());
        taskResponseDto.setPriority(e.getPriority());
        taskResponseDto.setTimestamp(e.getTimestamp());
        taskResponseDto.setOwner(userResponseMapper.toDto(e.getOwner()));
        taskResponseDto.setExecutor(userResponseMapper.toDto(e.getExecutor()));
        taskResponseDto.setComments(e.getComments().stream().map(commentResponseMapper::toDto).collect(Collectors.toList()));
        return taskResponseDto;
    }
}
