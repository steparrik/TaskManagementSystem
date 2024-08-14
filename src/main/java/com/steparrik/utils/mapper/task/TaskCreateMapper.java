package com.steparrik.utils.mapper.task;

import com.steparrik.dto.task.TaskCreateDto;
import com.steparrik.entity.Task;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TaskCreateMapper {
    public Task toEntity(TaskCreateDto d) {
        if (d == null) {
            return null;
        }
        Task task = new Task();
        task.setTitle(d.getTitle());
        task.setDescription(d.getDescription());
        task.setStatus(d.getStatus());
        task.setPriority(d.getPriority());
        task.setTimestamp(LocalDateTime.now());
        return task;
    }

}
